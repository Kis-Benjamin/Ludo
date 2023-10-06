package hu.bme.aut.android.ludocompose.features.local.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.domain.services.ScoreService
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.util.LoadingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameService: GameService,
    private val scoreService: ScoreService,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    val loadingState get() = loadingViewModel.state

    private suspend fun loadData() {
        val game = gameService.getActive()
        _state.update { state ->
            state.copy(
                game = game.toUiModel(),
                isSelectEnabled = game.isSelectEnabled,
            )
        }
    }

    fun select() {
        viewModelScope.launch {
            gameService.select()
            loadingViewModel.load()
        }
    }

    fun step(
        onGameEnded: () -> Unit,
    ) {
        viewModelScope.launch {
            val isFinished = gameService.step()
            loadingViewModel.load()
            if (isFinished) {
                val game = gameService.getActive()
                val winner = game.winner
                scoreService.save(winner)
                onGameEnded()
            }
        }
    }
}

data class GameState(
    val game: GameUi? = null,
    val isSelectEnabled: Boolean = false,
)