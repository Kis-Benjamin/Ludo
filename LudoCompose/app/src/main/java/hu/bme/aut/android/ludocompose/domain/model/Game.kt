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

import hu.bme.aut.android.ludocompose.data.model.GameEntity
import hu.bme.aut.android.ludocompose.data.model.GameWithPlayers

class Game internal constructor(
    private val game: GameEntity,
    private val players: List<Player>,
    private val board: Board,
    private val nextInt: () -> Int,
) {
    private var dice: Int
        get() = game.dice
        set(value) {
            game.dice = value
            board.dice.value = value
        }

    private var actPlayerIndex: Int
        get() = game.actPlayer
        set(value) {
            game.actPlayer = value
            board.dice.color = value
        }

    private val actPlayer: Player
        get() = players[actPlayerIndex]

    private val hasPlayerInGame get() = players.any { it.isInGame }

    private val playersInGame: Int get() = players.count { it.isInGame }

    private fun selectNextPlayer() {
        var playersChecked = 0
        do {
            actPlayerIndex = (actPlayerIndex + 1) % players.size
        } while (!actPlayer.isInGame && ++playersChecked < players.size)
    }

    private fun rollDice() {
        dice = nextInt() % 6 + 1
        updateSelectEnabled()
    }

    private fun updateSelectEnabled() {
        board.selectEnabled = actPlayer.isSelectEnabled(dice)
    }

    private fun setPointer() {
        actPlayer.setPointer(dice)
    }

    private fun clearPointer() {
        actPlayer.clearPointer()
    }

    init {
        setPointer()
        updateSelectEnabled()
    }

    fun getBoard(): Board {
        return board
    }

    fun select() {
        clearPointer()
        actPlayer.select(dice)
        setPointer()
    }

    fun step(): Boolean {
        clearPointer()
        val enteredHome = actPlayer.step(dice)
        if (enteredHome) {
            actPlayer.standing = players.size - playersInGame
        }
        if (dice != 6) selectNextPlayer()
        rollDice()
        actPlayer.select(dice)
        setPointer()
        return finished
    }

    val winnerName: String
        get() {
            check(!hasPlayerInGame) { "Game is not ended" }
            val winner = checkNotNull(players.find { it.standing == 1 }) { "No winner" }
            return winner.name
        }

    val finished: Boolean
        get() = !hasPlayerInGame
}

fun GameWithPlayers.toDomainModel(nextInt: () -> Int): Game {
    val board = Board(
        dice = game.toDomainDiceModel(),
    )
    val players = players.map { it.toDomainModel(board) }
    return Game(
        game = game,
        players = players,
        board = board,
        nextInt = nextInt,
    )
}
