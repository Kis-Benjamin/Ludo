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
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.BoardUi
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class GameViewModel(
    private val gameController: GameController,
) : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state = _state.asStateFlow()

    val loadingViewModel = LoadingViewModel(viewModelScope) {
        val board = gameController.getBoard()
        _state.update { state ->
            state.copy(
                board = board.toUiModel(),
                selectEnabled = board.selectEnabled,
                stepEnabled = board.stepEnabled,
            )
        }
    }

    protected val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent get() = uiEventViewModel.uiEvent

    protected fun load() {
        loadingViewModel.load()
    }

    protected open suspend fun selectImpl() {
        gameController.select()
    }

    protected open suspend fun stepImpl() {
        gameController.step()
    }

    fun select() = uiEventViewModel.handleWith {
        selectImpl()
        UiEvent.Continue
    }

    fun step() = uiEventViewModel.handleWith {
        stepImpl()
        UiEvent.Continue
    }

    fun end() = uiEventViewModel.handleWith {
        gameController.unLoad()
        UiEvent.Success
    }
}

data class GameState(
    val board: BoardUi = BoardUi(),
    val selectEnabled: Boolean = false,
    val stepEnabled: Boolean = false,
)