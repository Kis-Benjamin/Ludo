package hu.bme.aut.android.ludocompose.features.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.usecases.DeleteScoreUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadScoresUseCase
import hu.bme.aut.android.ludocompose.ui.model.ScoreItemUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.model.toUiText
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreBoardViewModel @Inject constructor(
    private val loadScoresUseCase: LoadScoresUseCase,
    private val deleteScoreUseCase: DeleteScoreUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ScoreBoardState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val games = loadScoresUseCase().getOrThrow().map { it.toUiModel() }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            scores = games,
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e,
                    )
                }
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    deleteScoreUseCase(id)
                    _state.update { state ->
                        state.copy(
                            scores = state.scores.filter { it.id != id },
                        )
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}

data class ScoreBoardState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val scores: List<ScoreItemUi> = emptyList(),
) {
    val isError: Boolean get() = error != null
}
