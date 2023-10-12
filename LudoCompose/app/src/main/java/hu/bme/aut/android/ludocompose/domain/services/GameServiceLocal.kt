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

package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.converters.duplicate
import hu.bme.aut.android.ludocompose.domain.converters.toDataModel
import hu.bme.aut.android.ludocompose.domain.converters.toDomainListModel
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import hu.bme.aut.android.ludocompose.domain.converters.update
import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import javax.inject.Inject

class GameServiceLocal @Inject constructor(
    private val gameRepository: GameRepository,
) : GameService {
    override suspend fun get(id: Long): Game {
        require(id > 0) { "Id must be positive" }
        val gameEntity = gameRepository.get(id)
        return gameEntity.toDomainModel()
    }

    override suspend fun getAll(): List<GameListItem> {
        val gameEntities = gameRepository.getAll()
        return gameEntities.map { it.toDomainListModel() }
    }

    override suspend fun start(playerNames: List<String>): Long {
        require(playerNames.size in 2..4) {
            "Number of players must be between 2 and 4"
        }
        require(playerNames.all { it.isNotBlank() }) {
            "Player name must not be blank"
        }
        val game = Game(playerNames)
        game.rollDice()
        val gameEntity = game.toDataModel("ONGOING")
        return gameRepository.insert(gameEntity)
    }

    override suspend fun save(id: Long, name: String?): Long {
        require(id > 0) { "Id must be positive" }
        val name = if (name != null) {
            require(name.isNotBlank()) { "Name must not be blank" }
            require(gameRepository.get(name) == null) { "Game save already exists:\n$name" }
            name
        } else "ONGOING"
        val gameEntity = gameRepository.get(id)
        val newGameEntity = gameEntity.duplicate(name)
        return gameRepository.insert(newGameEntity)
    }

    override suspend fun delete(id: Long) {
        require(id > 0) { "Id must be positive" }
        gameRepository.delete(id)
    }

    override suspend fun select(id: Long) {
        require(id > 0) { "Id must be positive" }
        val gameEntity = gameRepository.get(id).run {
            val game = this.toDomainModel()
            game.select()
            update(game)
        }
        gameRepository.update(gameEntity)
    }

    override suspend fun step(id: Long): Boolean {
        require(id > 0) { "Id must be positive" }
        val result: Boolean
        val gameEntity = gameRepository.get(id).run {
            val game = this.toDomainModel()
            result = game.step()
            update(game)
        }
        gameRepository.update(gameEntity)
        return result
    }
}