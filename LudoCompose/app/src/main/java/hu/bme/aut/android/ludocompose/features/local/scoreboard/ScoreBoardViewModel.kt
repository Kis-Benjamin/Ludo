package hu.bme.aut.android.ludocompose.features.local.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.usecases.DeleteScoreUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadScoresUseCase
import hu.bme.aut.android.ludocompose.ui.model.ScoreItemUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.util.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.util.UiEventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScoreBoardViewModel @Inject constructor(
    private val loadScoresUseCase: LoadScoresUseCase,
    private val deleteScoreUseCase: DeleteScoreUseCase,
) : ViewModel() {

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
        events = mapOf(
            "delete" to ::deleteEvent,
        ),
    )

    val loadingState get() = loadingViewModel.state

    val uiEvent get() = uiEventViewModel.uiEvent

    private val _state = MutableStateFlow(ScoreBoardState())
    val state = _state.asStateFlow()

    private suspend fun loadData() {
        val games = loadScoresUseCase().getOrThrow().map { it.toUiModel() }
        _state.update {
            it.copy(scores = games)
        }
    }

    private suspend fun deleteEvent(data: Any?) {
        val id = data as Long
        deleteScoreUseCase(id)
        loadingViewModel.load(true)
    }

    fun delete(id: Long) {
        uiEventViewModel.fire("delete", id)
    }
}

data class ScoreBoardState(
    val scores: List<ScoreItemUi> = emptyList(),
)
