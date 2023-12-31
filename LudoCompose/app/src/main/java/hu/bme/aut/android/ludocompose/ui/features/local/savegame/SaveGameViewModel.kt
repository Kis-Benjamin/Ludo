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

package hu.bme.aut.android.ludocompose.ui.features.local.savegame

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
class SaveGameViewModel @Inject constructor(
    private val gameManager: GameManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SaveGameState())
    val state = _state.asStateFlow()

    private val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent = uiEventViewModel.uiEvent

    fun save() = uiEventViewModel.handleWith {
        val name = _state.value.name
        if (name.isBlank()) {
            val message = UiText.StringResource(R.string.save_game_name_empty)
            UiEvent.Failure(message)
        } else {
            // val message = UiText.StringResource(R.string.save_game_name_exists, name)
            gameManager.save(name)
            UiEvent.Success
        }
    }

    fun setName(name: String) {
        _state.update { state ->
            state.copy(name = name)
        }
    }
}

data class SaveGameState(
    val name: String = "",
)
