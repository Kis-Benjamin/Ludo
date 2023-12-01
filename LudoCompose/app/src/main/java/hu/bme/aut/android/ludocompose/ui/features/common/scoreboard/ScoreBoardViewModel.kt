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
import hu.bme.aut.android.ludocompose.session.controller.ScoreController
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventViewModel
import hu.bme.aut.android.ludocompose.ui.model.ScoreUi
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ScoreBoardViewModel(
    private val scoreController: ScoreController
) : ViewModel() {

    private val _state = MutableStateFlow(ScoreBoardState())
    val state = _state.asStateFlow()

    val loadingViewModel = LoadingViewModel(viewModelScope) {
        val scoreDTOs = scoreController.getAll()
        val scores = scoreDTOs
            .sortedByDescending { it.winCount }
            .map { it.toUiModel() }
        _state.update { state ->
            state.copy(scores = scores)
        }
    }
}

data class ScoreBoardState(
    val scores: List<ScoreUi> = emptyList(),
)
