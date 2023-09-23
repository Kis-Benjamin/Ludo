package hu.bme.aut.android.ludocompose.features.savegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.usecases.SaveGameUseCase
import hu.bme.aut.android.ludocompose.ui.model.UiText
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
class SaveGameViewModel @Inject constructor(
    private val saveGameUseCase: SaveGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SaveGameState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun save() {
        viewModelScope.launch {
            val name = _state.value.name
            if (name.isBlank()) {
                _uiEvent.send(UiEvent.Failure(UiText.StringResource(R.string.save_game_name_empty)))
                return@launch
            }
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                try {
                    saveGameUseCase(name).getOrThrow()
                    _uiEvent.send(UiEvent.Success)
                } catch (e: IllegalArgumentException) {
                    _uiEvent.send(UiEvent.Failure(
                        UiText.StringResource(R.string.save_game_name_exists, name)
                    ))
                    return@launch
                } catch (e: Exception) {
                    _uiEvent.send(UiEvent.Failure(e.toUiText()))
                }
            }
        }
    }

    fun setName(name: String) {
        _state.update { state ->
            state.copy(name = name)
        }
    }
}

data class SaveGameState(
    val name: String = "",
)
