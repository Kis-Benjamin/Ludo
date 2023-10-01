package hu.bme.aut.android.ludocompose.features.local.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.domain.model.isSelectEnabled
import hu.bme.aut.android.ludocompose.domain.usecases.GameSelectUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GameStepUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GetGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.SaveScoreUseCase
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            delay(1000)
            load()
        }
    }

    private fun load() {
        viewModelScope.launch {
            CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                try {
                    val game = getGameUseCase().getOrThrow()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            game = game.toUiModel(),
                            isSelectEnabled = game.isSelectEnabled,
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(isLoading = false, error = e)
                    }
                }
            }
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
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val game: GameUi? = null,
    val isSelectEnabled: Boolean = false,
)

val GameState.isError: Boolean get() = error != null

