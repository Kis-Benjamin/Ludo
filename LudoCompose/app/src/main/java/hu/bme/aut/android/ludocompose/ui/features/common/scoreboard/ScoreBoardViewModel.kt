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

package hu.bme.aut.android.ludocompose.ui.features.common.scoreboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.session.controllers.ScoreController
import hu.bme.aut.android.ludocompose.ui.converters.toUiModel
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.ScoreItemUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ScoreBoardViewModel(
    private val scoreController: ScoreController
) : ViewModel() {

    private val _state = MutableStateFlow(ScoreBoardState())
    val state = _state.asStateFlow()

    private val loadingViewModel = LoadingViewModel(
        coroutineScope = viewModelScope,
        loadData = ::loadData,
    )

    private val uiEventViewModel = UiEventViewModel(
        coroutineScope = viewModelScope,
        events = mapOf(
            "delete" to ::deleteEvent,
        ),
    )

    val loadingState get() = loadingViewModel.state

    val uiEvent get() = uiEventViewModel.uiEvent

    private suspend fun loadData() {
        val scoreDtos = scoreController.getAll()
        val scores = scoreDtos.map { it.toUiModel() }
        _state.update { state ->
            state.copy(scores = scores)
        }
    }
    private suspend fun deleteEvent(data: Any?): UiEvent {
        val id = data as Long
        scoreController.delete(id)
        loadingViewModel.load()
        val message = UiText.StringResource(R.string.score_board_delete_success)
        return UiEvent.Settled(message)
    }

    fun delete(id: Long) {
        uiEventViewModel.fire("delete", id)
    }
}

data class ScoreBoardState(
    val scores: List<ScoreItemUi> = emptyList(),
)
