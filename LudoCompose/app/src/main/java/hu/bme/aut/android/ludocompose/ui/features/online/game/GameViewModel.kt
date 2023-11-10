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

package hu.bme.aut.android.ludocompose.ui.features.online.game

import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.session.di.Online
import hu.bme.aut.android.ludocompose.session.model.GameEnd
import hu.bme.aut.android.ludocompose.session.model.GameUpdate
import hu.bme.aut.android.ludocompose.session.stomp.StompManager
import hu.bme.aut.android.ludocompose.ui.features.common.game.GameViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import hu.bme.aut.android.ludocompose.ui.model.UiText
import kotlinx.coroutines.flow.first
import org.hildan.krossbow.stomp.conversions.subscribe
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    @Online
    gameController: GameController,
    private val stompManager: StompManager,
) : GameViewModel(
    gameController = gameController,
) {
    init {
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<GameUpdate>("/game/update")
                .collect {
                    if (it.id == gameController.id) {
                        load()
                    }
                }
            UiEvent.Failure(UiText.DynamicString("Stomp session closed"))
        }
        uiEventViewModel.handleWith {
            stompManager.session
                .subscribe<GameEnd>("/game/end")
                .first { it.id == gameController.id }.let {
                    end()
                }
            UiEvent.Close
        }
    }
}
