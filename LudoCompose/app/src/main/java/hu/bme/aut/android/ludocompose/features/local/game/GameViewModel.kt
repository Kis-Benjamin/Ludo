package hu.bme.aut.android.ludocompose.features.local.game

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.usecases.GameSelectUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GameStepUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GetGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.SaveScoreUseCase
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.util.LoadingState
import hu.bme.aut.android.ludocompose.ui.util.LoadingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameSelectUseCase: GameSelectUseCase,
    private val gameStepUseCase: GameStepUseCase,
    private val getGameUseCase: GetGameUseCase,
    private val saveScoreUseCase: SaveScoreUseCase,
) : LoadingViewModel<GameState>() {

    override val _state = MutableStateFlow(GameState())
    override val state = _state.asStateFlow()

    init {
        init()
    }

    override suspend fun loadImpl() {
        val game = getGameUseCase().getOrThrow()
        _state.update {
            it.copy(
                isLoading = false,
                game = game.toUiModel(),
                isSelectEnabled = game.isValidStep(),
            )
        }
    }

    fun select() {
        viewModelScope.launch {
            gameSelectUseCase()
            load()
        }
    }

    fun step(
        onGameEnded: () -> Unit,
    ) {
        viewModelScope.launch {
            val isFinished = gameStepUseCase()
            load()
            if (isFinished == true) {
                saveScoreUseCase()
                onGameEnded()
            }
        }
    }
}

data class GameState(
    override val isLoading: Boolean = false,
    override val error: Throwable? = null,
    val game: GameUi? = null,
    val isSelectEnabled: Boolean = false,
) : LoadingState {
    override val isError: Boolean get() = error != null

    override fun clone(isLoading: Boolean, error: Throwable?): LoadingState =
        copy(isLoading = isLoading, error = error)
}
