package hu.bme.aut.android.ludocompose.features.local.savegame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import hu.bme.aut.android.ludocompose.ui.util.UiEventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SaveGameViewModel @Inject constructor(
    private val gameService: GameService,
) : ViewModel() {

    private val _state = MutableStateFlow(SaveGameState())
    val state = _state.asStateFlow()

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
        events = mapOf(
            "save" to ::saveEvent,
        )
    )

    val uiEvent = uiEventViewModel.uiEvent

    private suspend fun saveEvent(data: Any?): UiEvent {
        val name = _state.value.name
        if (name.isBlank()) {
            val message = UiText.StringResource(R.string.save_game_name_empty)
            return UiEvent.Failure(message)
        }
        val message = UiText.StringResource(R.string.save_game_name_exists, name)
        gameService.save(name)
        return UiEvent.Success
    }

    fun save() {
        uiEventViewModel.fire("save")
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
