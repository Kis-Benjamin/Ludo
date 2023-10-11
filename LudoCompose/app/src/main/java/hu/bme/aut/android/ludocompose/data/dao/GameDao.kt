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
interface GameDao {
    @Transaction
    @Query("SELECT * FROM games WHERE name != 'ONGOING'")
    suspend fun getAll(): List<GameEntity>

    @Transaction
    @Query("SELECT * FROM games WHERE name = :name")
    suspend fun get(name: String): GameEntity?

    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun get(id: Long): GameWithPlayers

    @Insert
    suspend fun insert(game: GameEntity): Long

    @Insert
    suspend fun insert(player: PlayerEntity): Long

    @Insert
    suspend fun insert(token: TokenEntity): Long

    @Update
    suspend fun update(game: GameEntity)

    @Update
    suspend fun update(player: PlayerEntity)

    @Update
    suspend fun update(token: TokenEntity)

    @Transaction
    @Query("DELETE FROM games WHERE id = :id")
    suspend fun delete(id: Long)
}
