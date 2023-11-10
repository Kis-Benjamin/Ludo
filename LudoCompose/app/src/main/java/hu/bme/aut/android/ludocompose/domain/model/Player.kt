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

import hu.bme.aut.android.ludocompose.data.model.PlayerEntity
import hu.bme.aut.android.ludocompose.data.model.PlayerWithPieces

class Player internal constructor(
    private val player: PlayerEntity,
    private val pieces: List<Piece>,
) {
    private var actPieceIndex: Int
        get() = player.actPiece
        set(value) {
            player.actPiece = value
        }

    private val actPiece: Piece
        get() = pieces[actPieceIndex]

    private fun selectNextPiece(dice: Int) {
        var piecesChecked = 0
        do {
            actPieceIndex = (actPieceIndex + 1) % pieces.size
        } while (!actPiece.isValidMove(dice) && ++piecesChecked < pieces.size)
    }

    internal val isInGame: Boolean get() = pieces.any { it.isInGame }

    internal fun isSelectEnabled(dice: Int) = pieces.count { it.isValidMove(dice) } > 1

    internal fun setPointer(dice: Int) {
        actPiece.setPointer(dice)
    }

    internal fun clearPointer() {
        actPiece.clearPointer()
    }

    internal fun select(dice: Int) {
        selectNextPiece(dice)
    }

    internal fun step(dice: Int): Boolean {
        return actPiece.step(dice)
    }

    internal var standing
        get() = player.standing
        set(value) {
            player.standing = value
        }

    internal val name get() = player.name
}

fun PlayerWithPieces.toDomainModel(board: Board) = Player(
    player = player,
    pieces = pieces.map { it.toDomainModel(
        yardFields = board.yardFields(player.index),
        trackFields = board.trackFields(player.index),
        homeFields = board.homeFields(player.index),
        color = player.index,
    ) },
)
