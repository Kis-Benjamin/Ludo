package hu.bme.aut.android.ludocompose.features.local.loadgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.model.Board
import hu.bme.aut.android.ludocompose.domain.usecases.DeleteGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadGamesUseCase
import hu.bme.aut.android.ludocompose.ui.model.GameListItemUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
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
class LoadGameViewModel @Inject constructor(
    private val loadGamesUseCase: LoadGamesUseCase,
    private val loadGameUseCase: LoadGameUseCase,
    private val deleteGameUseCase: DeleteGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoadGameState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadAll() {
        viewModelScope.launch {
            _state.update { state ->
                state.copy(isLoading = true)
            }
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    val games = loadGamesUseCase().getOrThrow().map { it.toUiModel() }
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            games = games,
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e,
                    )
                }
            }
        }
    }

    fun select(index: Int) {
        _state.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun loadSelected() {
        viewModelScope.launch {
            try {
                val selectedId = _state.value.selectedId
                if (selectedId == null) {
                    _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.load_game_empty_selection)))
                    return@launch
                }
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    loadGameUseCase(selectedId)
                }
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    deleteGameUseCase(id)
                    _state.update { state ->
                        state.copy(
                            games = state.games.filter { it.id != id },
                            selectedIndex = null,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.Failure(e.toUiText()))
            }
        }
    }
}

data class LoadGameState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val games: List<GameListItemUi> = emptyList(),
    val selectedIndex: Int? = null,
) {
    val isError: Boolean get() = error != null
    val selectedId: Long?
        get() = selectedIndex?.let { games.getOrNull(it)?.id }
}
