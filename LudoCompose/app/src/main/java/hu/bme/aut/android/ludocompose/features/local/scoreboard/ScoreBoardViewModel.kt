package hu.bme.aut.android.ludocompose.features.local.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.services.ScoreService
import hu.bme.aut.android.ludocompose.ui.model.ScoreItemUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.util.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import hu.bme.aut.android.ludocompose.ui.util.UiEventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScoreBoardViewModel @Inject constructor(
    private val scoreService: ScoreService,
) : ViewModel() {

    private val _state = MutableStateFlow(ScoreBoardState())
    val state = _state.asStateFlow()

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

    private suspend fun loadData() {
        val scores = scoreService.getAll().map { it.toUiModel() }
        _state.update { state ->
            state.copy(scores = scores)
        }
    }
    private suspend fun deleteEvent(data: Any?): UiEvent {
        val id = data as Long
        scoreService.delete(id)
        loadingViewModel.load()
        val message = UiText.StringResource(R.string.score_board_delete_success)
        return UiEvent.Settled(message)
    }

    fun delete(id: Long) {
        uiEventViewModel.fire("delete", id)
    }
}

data class ScoreBoardState(
    val scores: List<ScoreItemUi> = emptyList(),
)
