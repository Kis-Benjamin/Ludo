/*
 * Copyright © 2023 Benjamin Kis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.bme.aut.android.ludocompose.ui.features.common.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.ludocompose.session.controllers.GameController
import hu.bme.aut.android.ludocompose.session.controllers.ScoreController
import hu.bme.aut.android.ludocompose.ui.converters.toUiModel
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class GameViewModel(
    private val gameController: GameController,
    private val scoreController: ScoreController,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    val loadingState get() = loadingViewModel.state

    private suspend fun loadData() {
        val game = gameController.getActive()
        _state.update { state ->
            state.copy(
                game = game.toUiModel(),
                isSelectEnabled = game.isSelectEnabled,
            )
        }
    }

    fun select() {
        viewModelScope.launch {
            gameController.select()
            loadingViewModel.load()
        }
    }

    private var ended = false

    fun step(
        onGameEnded: () -> Unit,
    ) {
        viewModelScope.launch {
            val isFinished = gameController.step()
            loadingViewModel.load()
            if (isFinished && !ended) {
                ended = true
                val game = gameController.getActive()
                val winner = game.winner
                scoreController.save(winner)
                gameController.unLoad()
                onGameEnded()
            }
        }
    }
}

data class GameState(
    val game: GameUi? = null,
    val isSelectEnabled: Boolean = false,
)