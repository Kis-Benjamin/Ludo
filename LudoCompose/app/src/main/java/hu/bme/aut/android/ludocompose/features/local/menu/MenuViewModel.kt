package hu.bme.aut.android.ludocompose.features.local.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.services.GameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val gameService: GameService,
) : ViewModel() {
    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            val hasActiveGame = gameService.hasActive
            _state.update { state ->
                state.copy(
                    hasActiveGame = hasActiveGame,
                )
            }
        }
    }
}

data class MenuState(
    val hasActiveGame: Boolean = false,
)
