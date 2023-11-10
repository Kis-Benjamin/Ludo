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

package hu.bme.aut.android.ludocompose.data.datasource

import hu.bme.aut.android.ludocompose.data.dao.GameDao
import hu.bme.aut.android.ludocompose.data.model.GameEntity
import hu.bme.aut.android.ludocompose.data.model.GameWithPlayers
import javax.inject.Inject

class GameRepositoryDatabase @Inject constructor(
    private val gameDao: GameDao
) : GameRepository {
    override suspend fun getAll(exceptName: String) = gameDao.getAll(exceptName)

    override suspend fun get(name: String): GameEntity? {
        require(name.isNotBlank()) { "Name must not be blank" }
        return gameDao.get(name)
    }

    override suspend fun get(id: Long): GameWithPlayers {
        require(id > 0) { "Id must be positive" }
        return gameDao.get(id)
    }

    override suspend fun insert(gameWithPlayers: GameWithPlayers): Long {
        require(
            gameWithPlayers.game.id == null &&
            gameWithPlayers.players.all {
                it.player.id == null &&
                it.player.gameId == null &&
                it.pieces.all {
                    it.id == null &&
                    it.playerId == null
                }
            }
        ) { "Id must be null" }
        require(gameWithPlayers.players.size in 2..4) {
            "Number of players must be between 2 and 4"
        }
        require(gameWithPlayers.players.all { it.player.name.isNotBlank() }) {
            "Player name must not be blank"
        }
        return gameDao.insert(gameWithPlayers)
    }

    override suspend fun update(gameWithPlayers: GameWithPlayers) {
        require(
            gameWithPlayers.game.id != null &&
            gameWithPlayers.players.all {
                it.player.id != null &&
                it.player.gameId != null &&
                it.pieces.all {
                    it.id != null &&
                    it.playerId != null
                }
            }
        ) { "Id must not be null" }
        require(
            gameWithPlayers.game.id > 0 &&
            gameWithPlayers.players.all {
                it.player.id!! > 0 &&
                it.player.gameId!! > 0 &&
                it.pieces.all {
                    it.id!! > 0 &&
                    it.playerId!! > 0
                }
            }
        ) { "Id must be positive" }
        gameDao.update(gameWithPlayers)
    }

    override suspend fun delete(id: Long) {
        require(id > 0) { "Id must be positive" }
        gameDao.delete(id)
    }
}