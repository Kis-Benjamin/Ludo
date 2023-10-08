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

package hu.bme.aut.android.ludocompose.ui.converters

import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import hu.bme.aut.android.ludocompose.ui.model.BoardUi
import hu.bme.aut.android.ludocompose.ui.model.ColorSequence
import hu.bme.aut.android.ludocompose.ui.model.DiceUi
import hu.bme.aut.android.ludocompose.ui.model.GameListItemUi
import hu.bme.aut.android.ludocompose.ui.model.GameUi


fun GameListItem.toUiModel() = GameListItemUi(
    id = id,
    name = name,
    date = date.toString(),
    playerNames = playerNames,
)

fun Game.toUiModel() = GameUi(
    boardUi = BoardUi.apply { update(board) },
    diceUi = DiceUi(dice.toString(), ColorSequence.entries[actPlayerIndex]),
)
