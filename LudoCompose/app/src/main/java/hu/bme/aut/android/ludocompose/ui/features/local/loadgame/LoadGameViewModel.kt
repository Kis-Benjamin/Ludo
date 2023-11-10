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

package hu.bme.aut.android.ludocompose.ui.features.local.loadgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.manager.GameManager
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoadGameViewModel @Inject constructor(
    private val gameManager: GameManager,
) : ViewModel() {

    private val _state = MutableStateFlow(LoadGameState())
    val state = _state.asStateFlow()

    val loadingViewModel = LoadingViewModel(viewModelScope) {
        val gameDTOs = gameManager.getList()
        val games = gameDTOs.map { it.toUiModel() }
        _state.update { state ->
            state.copy(games = games)
        }
    }

    private val uiEventViewModel = UiEventViewModel(viewModelScope)

    val uiEvent get() = uiEventViewModel.uiEvent

    fun select(index: Int) {
        _state.update { state ->
            state.copy(selectedIndex = index)
        }
    }

    fun load() = uiEventViewModel.handleWith {
        val selectedId = _state.value.selectedId
        if (selectedId == null) {
            val message = UiText.StringResource(R.string.load_game_empty_selection)
            UiEvent.Failure(message)
        } else {
            gameManager.load(selectedId)
            UiEvent.Success
        }
    }

    fun delete(id: Long) = uiEventViewModel.handleWith {
        gameManager.delete(id)
        loadingViewModel.load()
        val message = UiText.StringResource(R.string.load_game_delete_success)
        UiEvent.Concluded(message)
    }
}

data class LoadGameState(
    val games: List<GameUi> = emptyList(),
    val selectedIndex: Int? = null,
) {
    val selectedId: Long?
        get() = selectedIndex?.let { games.getOrNull(it)?.id }
}
