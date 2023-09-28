package hu.bme.aut.android.ludocompose.features.newgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.usecases.StartGameUseCase
import hu.bme.aut.android.ludocompose.ui.model.UiText
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
class NewGameViewModel @Inject constructor(
    private val startGameUseCase: StartGameUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(NewGameState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun changePlayerCount(playerCount: Int) {
        _state.update { state ->
            state.copy(
                playerCount = playerCount.coerceIn(2, 4),
            )
        }
    }

    fun changePlayerName(playerIndex: Int, playerName: String) {
        _state.update { state ->
            state.copy(
                playerNames = state.playerNames.toMutableList().apply {
                    this[playerIndex] = playerName
                }
            )
        }
    }

    fun startGame() {
        viewModelScope.launch {
            _state.value.playerNames.forEachIndexed { index, name ->
                if (name.isBlank() && index < _state.value.playerCount) {
                    _uiEvent.send(
                        UiEvent.Failure(
                            UiText.StringResource(R.string.new_game_player_names_empty, index + 1)
                        )
                    )
                    return@launch
                }
            }
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                val playerCount = _state.value.playerCount
                val playerNames = _state.value.playerNames
                startGameUseCase(playerCount, playerNames.take(playerCount))
            }
            _uiEvent.send(UiEvent.Success)
        }
    }
}

data class NewGameState(
    val playerCount: Int = 2,
    val playerNames: List<String> = listOf("", "", "", ""),
)
