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

package hu.bme.aut.android.ludocompose.data.dao

import androidx.room.*
import hu.bme.aut.android.ludocompose.data.model.GameEntity
import hu.bme.aut.android.ludocompose.data.model.PlayerEntity
import hu.bme.aut.android.ludocompose.data.model.TokenEntity
import hu.bme.aut.android.ludocompose.data.model.GameWithPlayers

@Dao
abstract class GameDao {
    @Transaction
    @Query("SELECT * FROM games WHERE name != 'ONGOING'")
    abstract suspend fun getAll(): List<GameEntity>

    @Query("SELECT * FROM games WHERE name = :name")
    abstract suspend fun get(name: String): GameEntity?

    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    abstract suspend fun get(id: Long): GameWithPlayers

    @Insert
    internal abstract suspend fun insert(game: GameEntity): Long

    @Insert
    internal abstract suspend fun insert(player: PlayerEntity): Long

    @Insert
    internal abstract suspend fun insert(token: TokenEntity): Long

    @Transaction
    open suspend fun insert(gameWithPlayers: GameWithPlayers): Long {
        val id = insert(gameWithPlayers.game)
        gameWithPlayers.players.map {
            it.copy(player = it.player.copy(gameId = id))
        }.forEach { playerWithTokens ->
            val id = insert(playerWithTokens.player)
            playerWithTokens.tokens.map {
                it.copy(playerId = id)
            }.forEach {
                insert(it)
            }
        }
        return id
    }

    @Update
    internal abstract suspend fun update(game: GameEntity)

    @Update
    internal abstract suspend fun update(player: PlayerEntity)

    @Update
    internal abstract suspend fun update(token: TokenEntity)

    @Transaction
    open suspend fun update(gameWithPlayers: GameWithPlayers) {
        update(gameWithPlayers.game)
        gameWithPlayers.players.forEach { playerWithTokens ->
            update(playerWithTokens.player)
            playerWithTokens.tokens.forEach { token ->
                update(token)
            }
        }
    }

    @Transaction
    @Query("DELETE FROM games WHERE id = :id")
    abstract suspend fun delete(id: Long)
}
