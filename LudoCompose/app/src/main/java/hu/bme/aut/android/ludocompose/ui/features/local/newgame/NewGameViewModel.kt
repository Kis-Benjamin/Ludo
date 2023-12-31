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

package hu.bme.aut.android.ludocompose.ui.features.local.newgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.manager.GameManager
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NewGameViewModel @Inject constructor(
    private val gameManager: GameManager,
) : ViewModel() {

    private val _state = MutableStateFlow(NewGameState())
    val state = _state.asStateFlow()

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
    )

    val uiEvent = uiEventViewModel.uiEvent

    fun changePlayerCount(playerCount: Int) {
        _state.update { state ->
            state.copy(
                playerCount = playerCount.coerceIn(2, 4),
            )
        }
    }

    fun changePlayerName(playerIndex: Int, playerName: String) {
        _state.update { state ->
            state.copy(
                playerNames = state.playerNames.toMutableList().apply {
                    this[playerIndex] = playerName
                }
            )
        }
    }

    fun startGame() = uiEventViewModel.handleWith {
        _state.value.playerNames.forEachIndexed { index, name ->
            if (name.isBlank() && index < _state.value.playerCount) {
                val message =
                    UiText.StringResource(R.string.new_game_player_names_empty, index + 1)
                return@handleWith UiEvent.Failure(message)
            }
        }
        val playerCount = _state.value.playerCount
        val playerNames = _state.value.playerNames
        gameManager.start(playerNames.take(playerCount))
        UiEvent.Success
    }
}

data class NewGameState(
    val playerCount: Int = 2,
    val playerNames: List<String> = listOf("", "", "", ""),
)
