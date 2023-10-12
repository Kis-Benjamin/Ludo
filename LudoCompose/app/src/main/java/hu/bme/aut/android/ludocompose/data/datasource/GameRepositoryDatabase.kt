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
import hu.bme.aut.android.ludocompose.data.model.PlayerWithTokens
import javax.inject.Inject

class GameRepositoryDatabase @Inject constructor(
    private val gameDao: GameDao
) : GameRepository {
    override suspend fun getAll() = gameDao.getAll()

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
                it.tokens.all {
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
        val id = gameDao.insert(gameWithPlayers.game)
        gameWithPlayers.players.map {
            it.copy(player = it.player.copy(gameId = id))
        }.forEach { playerWithTokens ->
            val id = gameDao.insert(playerWithTokens.player)
            playerWithTokens.tokens.map {
                it.copy(playerId = id)
            }.forEach {
                gameDao.insert(it)
            }
        }
        return id
    }

    override suspend fun update(gameWithPlayers: GameWithPlayers) {
        require(
            gameWithPlayers.game.id != null &&
            gameWithPlayers.players.all {
                it.player.id != null &&
                it.player.gameId != null &&
                it.tokens.all {
                    it.id != null &&
                    it.playerId != null
                }
            }
        ) { "Id must not be null" }
        require(
            gameWithPlayers.game.id > 0 &&
            gameWithPlayers.players.all {
                it.player.id!! > 0 &&
                it.tokens.all {
                    it.id!! > 0
                }
            }
        ) { "Id must be positive" }
        gameDao.update(gameWithPlayers.game)
        gameWithPlayers.players.forEach { playerWithTokens ->
            gameDao.update(playerWithTokens.player)
            playerWithTokens.tokens.forEach { token ->
                gameDao.update(token)
            }
        }
    }

    override suspend fun delete(id: Long) {
        require(id > 0) { "Id must be positive" }
        gameDao.delete(id)
    }
}