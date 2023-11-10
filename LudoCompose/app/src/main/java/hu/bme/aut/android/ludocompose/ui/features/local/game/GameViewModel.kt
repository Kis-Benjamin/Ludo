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

package hu.bme.aut.android.ludocompose.ui.features.local.game

import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.ludocompose.session.controller.GameController
import hu.bme.aut.android.ludocompose.session.di.Local
import hu.bme.aut.android.ludocompose.session.manager.GameManager
import hu.bme.aut.android.ludocompose.ui.features.common.game.GameViewModel
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEvent
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameManager: GameManager,
) : GameViewModel(
    gameController = gameManager,
) {
    override suspend fun selectImpl() {
        super.selectImpl()
        load()
    }

    override suspend fun stepImpl() {
        super.stepImpl()
        if (gameManager.isFinished()) {
            end()
        } else {
            load()
        }
    }
}
