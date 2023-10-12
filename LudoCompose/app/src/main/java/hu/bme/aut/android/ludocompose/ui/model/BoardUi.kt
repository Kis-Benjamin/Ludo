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

package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.ui.model.Constants.playerCount
import hu.bme.aut.android.ludocompose.ui.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.ui.model.Constants.trackMultiplier
import hu.bme.aut.android.ludocompose.ui.model.Constants.trackSize
import hu.bme.aut.android.ludocompose.session.model.GameDto
import hu.bme.aut.android.ludocompose.session.model.TokenDto

data object BoardUi {
    val yardFields: List<List<FieldUi>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                FieldUi(
                    Constants.yardPoints[playerIndex][tokenIndex],
                    ColorSequence.entries[playerIndex],
                )
            }
        }
    }

    val trackFields: List<FieldUi> by lazy {
        List(trackSize) { index ->
            FieldUi(
                Constants.trackPoints[index],
                if (index.mod(trackMultiplier) == 0)
                    ColorSequence.entries[index / trackMultiplier]
                else ColorSequence.WHITE,
            )
        }
    }

    val homeFields: List<List<FieldUi>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                FieldUi(
                    Constants.homePoints[playerIndex][tokenIndex],
                    ColorSequence.entries[playerIndex],
                )
            }
        }
    }

    private fun reset() {
        yardFields.forEach { it.forEach { it.reset() } }
        trackFields.forEach { it.reset() }
        homeFields.forEach { it.forEach { it.reset() } }
    }

    fun update(gameDto: GameDto) = apply {
        reset()
        val players = gameDto.players
        for (playerIndex in players.indices) {
            val player = players[playerIndex]
            var homeCount = 4
            for (tokenIndex in player.tokens.indices) {
                val token = player.tokens[tokenIndex]
                when (token.state) {
                    TokenDto.State.YARD -> {
                        yardFields[playerIndex][tokenIndex].tokenColorIndex = playerIndex
                    }
                    TokenDto.State.TRACK -> {
                        trackFields[token.trackPos % trackSize].tokenColorIndex = playerIndex
                    }
                    else -> {
                        homeFields[playerIndex][--homeCount].tokenColorIndex = playerIndex
                    }
                }
            }
        }
        if (gameDto.isValidStep) {
            val player = gameDto.actPlayer
            val token = gameDto.actPlayer.actToken
            if (token.isInYard) {
                yardFields[gameDto.actPlayerIndex][player.actTokenIndex].isPointer = true
            } else if (token.isInTrack) {
                trackFields[token.trackPos % trackSize].isPointer = true
            }
        }
    }
}
