package hu.bme.aut.android.ludocompose.features.local.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.usecases.GameSelectUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GameStepUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GetGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.SaveScoreUseCase
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import hu.bme.aut.android.ludocompose.ui.util.LoadingViewModel
import kotlinx.coroutines.Dispatchers
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
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    val loadingState get() = loadingViewModel.state

    private suspend fun loadData() = try {
        val game = getGameUseCase().getOrThrow()
        _state.update {
            it.copy(
                game = game.toUiModel(),
                isSelectEnabled = game.isSelectEnabled,
            )
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun select() {
        viewModelScope.launch {
            gameSelectUseCase()
            loadingViewModel.load()
        }
    }

    fun step(
        onGameEnded: () -> Unit,
    ) {
        viewModelScope.launch {
            val isFinished = gameStepUseCase()
            loadingViewModel.load()
            if (isFinished == true) {
                saveScoreUseCase()
                onGameEnded()
            }
        }
    }
}

data class GameState(
    val game: GameUi? = null,
    val isSelectEnabled: Boolean = false,
)