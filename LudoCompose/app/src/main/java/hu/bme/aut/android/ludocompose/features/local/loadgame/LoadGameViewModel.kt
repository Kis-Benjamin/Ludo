package hu.bme.aut.android.ludocompose.features.local.loadgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.ui.model.GameListItemUi
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
class LoadGameViewModel @Inject constructor(
    private val gameService: GameService,
) : ViewModel() {

    private val _state = MutableStateFlow(LoadGameState())
    val state = _state.asStateFlow()

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
        events = mapOf(
            "loadSelected" to ::loadSelectedEvent,
            "delete" to ::deleteEvent,
        ),
    )

    val loadingState get() = loadingViewModel.state

    val uiEvent get() = uiEventViewModel.uiEvent

    private suspend fun loadData() {
        val games = gameService.getList().map { it.toUiModel() }
        _state.update { state ->
            state.copy(games = games)
        }
    }
    fun select(index: Int) {
        _state.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    private suspend fun loadSelectedEvent(data: Any?): UiEvent {
        val selectedId = _state.value.selectedId
        if (selectedId == null) {
            val message = UiText.StringResource(R.string.load_game_empty_selection)
            return UiEvent.Failure(message)
        }
        gameService.load(selectedId)
        return UiEvent.Success
    }

    fun loadSelected() {
        uiEventViewModel.fire("loadSelected")
    }

    private suspend fun deleteEvent(data: Any?): UiEvent {
        val id = data as Long
        gameService.delete(id)
        loadingViewModel.load()
        val message = UiText.StringResource(R.string.load_game_delete_success)
        return UiEvent.Settled(message)
    }

    fun delete(id: Long) {
        uiEventViewModel.fire("delete", id)
    }
}

data class LoadGameState(
    val games: List<GameListItemUi> = emptyList(),
    val selectedIndex: Int? = null,
) {
    val selectedId: Long?
        get() = selectedIndex?.let { games.getOrNull(it)?.id }
}
