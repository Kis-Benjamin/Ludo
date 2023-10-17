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

package hu.bme.aut.android.ludocompose.session.model

data class GameDto(
    val players: List<PlayerDto>,
    var actPlayerIndex: Int = 0,
    var dice: Int = 0,
) {
    val actPlayer: PlayerDto get() = players[actPlayerIndex]

    private val isInYard get() = actPlayer.actToken.isInYard

    private val isInTrack get() = actPlayer.actToken.isInTrack

    private val canStepInTrack get() = isInYard && dice == 6

    val isValidStep get() = canStepInTrack || isInTrack

    val winner: String get() = players.find { it.standing == 1 }!!.name

    val isSelectEnabled: Boolean get() = actPlayer.tokens.count { it.isInYard && dice == 6 || it.isInTrack } > 1
}