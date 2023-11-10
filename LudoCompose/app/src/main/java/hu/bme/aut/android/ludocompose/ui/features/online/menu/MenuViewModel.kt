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

package hu.bme.aut.android.ludocompose.ui.features.online.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.session.di.Online
import hu.bme.aut.android.ludocompose.session.stomp.StompManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    @Online
    private val gameController: GameController,
    private val stompManager: StompManager,
) : ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            stompManager.initialize()
        }
        load()
    }

    fun load() {
        viewModelScope.launch {
            val hasActiveGame = gameController.hasActive
            _state.update { state ->
                state.copy(
                    hasActiveGame = hasActiveGame,
                )
            }
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            stompManager.dispose()
            gameController.unLoad()
        }
        super.onCleared()
    }
}

data class MenuState(
    val hasActiveGame: Boolean = false,
)
