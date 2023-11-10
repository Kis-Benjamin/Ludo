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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.diceValues
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.trackPositions
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.trackSize
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.PieceEntity

class Piece internal constructor(
    private val piece: PieceEntity,
    homeFields: List<Field>,
    private val trackFields: List<Field>,
    yardFields: List<Field>,
    internal val color: Int,
) {
    private var trackPos: Int
        get() = piece.trackPos
        set(value) {
            require(value in trackPositions) { "Invalid track position: $value" }
            piece.trackPos = value
        }

    enum class State {
        YARD, TRACK, HOME
    }

    private var state: State
        get() = when (piece.state) {
            0 -> State.YARD
            1 -> State.TRACK
            2 -> State.HOME
            else -> throw IllegalStateException("Invalid piece state: ${piece.state}")
        }
        set(value) {
            piece.state = value.ordinal
        }

    private val yardField = yardFields[piece.index]

    private val trackField: Field
        get() = trackFields[piece.trackPos]

    private val homeField = homeFields[piece.index]

    private val currentField: Field
        get() = when (state) {
            State.YARD -> yardField
            State.TRACK -> trackField
            State.HOME -> homeField
        }

    init {
        currentField.stepOnWith(this)
    }

    private inline fun move(updateState: () -> Unit) {
        currentField.stepOffWith(this)
        updateState()
        currentField.stepOnWith(this)
    }

    private fun move(dice: Int) {
        when (state) {
            State.YARD -> move {
                state = State.TRACK
                trackPos = 0
            }

            State.TRACK -> move {
                if (trackPos + dice < trackSize) {
                    trackPos += dice
                } else {
                    state = State.HOME
                }
            }

            // never happens, isValidMove would return false
            State.HOME -> throw IllegalStateException("Cannot move a piece that is already home")
        }
    }

    internal val isInGame: Boolean
        get() = state != State.HOME

    internal fun isValidMove(dice: Int) = run {
        require(dice in diceValues) { "Invalid dice value: $dice" }
        when (state) {
            State.YARD -> dice == 6
            State.TRACK -> true
            State.HOME -> false
        }
    }

    internal fun setPointer(dice: Int) {
        currentField.isPointer = isValidMove(dice)
    }

    internal fun step(dice: Int): Boolean {
        require(dice in diceValues) { "Invalid dice value: $dice" }
        if (isValidMove(dice)) move(dice)
        return state == State.HOME
    }

    internal fun kill() {
        check(state != State.YARD) { "Cannot kill a piece that is still in the yard" }
        check(state != State.HOME) { "Cannot kill a piece that is already home" }
        state = State.YARD
        yardField.stepOnWith(this)
    }
}

fun PieceEntity.toDomainModel(
    homeFields: List<Field>,
    trackFields: List<Field>,
    yardFields: List<Field>,
    color: Int,
) = Piece(
    piece = this,
    homeFields = homeFields,
    trackFields = trackFields,
    yardFields = yardFields,
    color = color,
)
