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

data class Field internal constructor(
    private var piece: Piece? = null,
    internal var isPointer: Boolean = false,
) {
    val pieceColor: Int? get() = piece?.color
    val pointer: Boolean get() = isPointer

    internal fun stepOnWith(piece: Piece?) {
        require(piece != null) { "Cannot step with null piece" }
        this.piece?.kill()
        this.piece = piece
    }

    internal fun stepOffWith(piece: Piece) {
        require(this.piece == piece) { "Cannot step off with other piece" }
        check(this.piece != null) { "No piece to step off" }
        this.piece = null
    }
}
