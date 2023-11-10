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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerCounts
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerMaxCount
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerMinCount
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.datasource.GameRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Board
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.GameEntity
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.toDomainModel
import org.springframework.stereotype.Service

@Service
class GameServiceRepository(
    private val gameRepository: GameRepository,
) : GameService {

    companion object {
        private val numSet = (1..46656).shuffled()
        private val nextInt = { numSet.random() }
    }

    override fun start(userDetails: List<Pair<String, String>>): Long {
        require(userDetails.size in playerCounts) { "Player count: ${userDetails.size}, must be between $playerMinCount and $playerMaxCount" }
        require(userDetails.map { it.first }.toSet().size == userDetails.size) { "User names must be unique" }
        require(userDetails.map { it.second }.toSet().size == userDetails.size) { "User ids must be unique" }
        require(userDetails.all { it.first.isNotBlank() }) { "User names must not be blank" }
        require(userDetails.all { it.second.isNotBlank() }) { "User ids must not be blank" }
        val gameEntity = GameEntity(userDetails).run {
            gameRepository.insert(this)
        }
        return gameEntity.id!!
    }

    override fun delete(id: Long) {
        require(id > 0) { "Id must be positive" }
        gameRepository.delete(id)
    }

    override fun getBoard(id: Long, userId: String): Board {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val gameEntity = gameRepository.get(id)
        val game = gameEntity.toDomainModel(nextInt)
        return game.getBoard(userId)
    }

    override fun select(id: Long, userId: String) {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val gameEntity = gameRepository.get(id)
        val game = gameEntity.toDomainModel(nextInt)
        game.select(userId)
        gameRepository.update(gameEntity)
    }

    override fun step(id: Long, userId: String): Boolean {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val gameEntity = gameRepository.get(id)
        val game = gameEntity.toDomainModel(nextInt)
        val ended = game.step(userId)
        gameRepository.update(gameEntity)
        return ended
    }

    override fun getWinner(id: Long): String {
        require(id > 0) { "Id must be positive" }
        val gameEntity = gameRepository.get(id)
        val game = gameEntity.toDomainModel(nextInt)
        return game.winnerName
    }
}