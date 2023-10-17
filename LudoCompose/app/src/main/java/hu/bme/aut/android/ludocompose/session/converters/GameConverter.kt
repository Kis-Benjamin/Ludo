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

package hu.bme.aut.android.ludocompose.session.converters

import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import hu.bme.aut.android.ludocompose.domain.model.Player
import hu.bme.aut.android.ludocompose.domain.model.Token
import hu.bme.aut.android.ludocompose.session.model.GameDto
import hu.bme.aut.android.ludocompose.session.model.GameListItemDto
import hu.bme.aut.android.ludocompose.session.model.PlayerDto
import hu.bme.aut.android.ludocompose.session.model.TokenDto

private fun Token.toSessionModel() = TokenDto(
    state = TokenDto.State.entries[state.ordinal],
    trackPos = trackPos,
)

private fun Player.toSessionModel() = PlayerDto(
    name = name,
    standing = standing,
    tokens = tokens.map { it.toSessionModel() },
    actTokenIndex = actTokenIndex,
)

fun Game.toSessionModel() = GameDto(
    players = players.map { it.toSessionModel() },
    actPlayerIndex = actPlayerIndex,
    dice = dice,
)

fun GameListItem.toSessionModel() = GameListItemDto(
    id = id,
    name = name,
    date = date,
    playerNames = playerNames,
)
