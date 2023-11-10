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

package hu.bme.aut.android.ludocompose.domain.model

import hu.bme.aut.android.ludocompose.common.model.Constants.pieceCount
import hu.bme.aut.android.ludocompose.common.model.Constants.playerCountIndices
import hu.bme.aut.android.ludocompose.common.model.Constants.playerMaxCount
import hu.bme.aut.android.ludocompose.common.model.Constants.trackMultiplier
import hu.bme.aut.android.ludocompose.common.model.Constants.trackSize

data class Board internal constructor(
    val dice: Dice = Dice(),

    var selectEnabled: Boolean = false,

    var stepEnabled: Boolean = true,

    val yardFields: List<List<Field>> =
        List(playerMaxCount) {
            List(pieceCount) {
                Field()
            }
        },

    val trackFields: List<Field> =
        List(trackSize) {
            Field()
        },

    val homeFields: List<List<Field>> =
        List(playerMaxCount) {
            List(pieceCount) {
                Field()
            }
        },
) {
    internal fun yardFields(playerIndex: Int): List<Field> {
        require(playerIndex in playerCountIndices) { "Invalid player index: $playerIndex" }
        return yardFields[playerIndex]
    }

    internal fun trackFields(playerIndex: Int): List<Field> {
        require(playerIndex in playerCountIndices) { "Invalid player index: $playerIndex" }
        return List(trackSize) {
            trackFields[
                (it + playerIndex * trackMultiplier) % trackSize
            ]
        }
    }

    internal fun homeFields(playerIndex: Int): List<Field> {
        require(playerIndex in playerCountIndices) { "Invalid player index: $playerIndex" }
        return homeFields[playerIndex]
    }
}
