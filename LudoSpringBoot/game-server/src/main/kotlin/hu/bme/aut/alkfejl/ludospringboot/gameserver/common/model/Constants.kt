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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model

data object Constants {
    const val playerMinCount = 2
    const val playerMaxCount = 4
    const val pieceCount = 4
    const val trackMultiplier = 10
    const val trackSize = trackMultiplier * playerMaxCount
    val playerCounts = playerMinCount..playerMaxCount
    val playerCountIndices = 0 until playerMaxCount
    val pieceIndices = 0 until pieceCount
    val standingIndices = 0 until playerMaxCount
    val trackPositions = 0 until trackSize
    val diceValues = 1..6
}
