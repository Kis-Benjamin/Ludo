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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.data.datasource

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.debug
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.GameEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.PieceEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.PlayerEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.GameEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
class GameRepositoryDatabase(
    private val gameEntityRepository: GameEntityRepository,
    private val playerEntityRepository: PlayerEntityRepository,
    private val pieceEntityRepository: PieceEntityRepository,
) : GameRepository {
    override fun get(id: Long): GameEntity {
        val game = gameEntityRepository.findById(id).run {
            requireNotNull(getOrNull()) { logger error "Game not found with id: $id" }
        }
        logger debug "Game found with id: $id"
        return game
    }

    @Transactional
    override fun insert(game: GameEntity): GameEntity {
        return game.copy(id = null, players = mutableListOf()).run {
            gameEntityRepository.save(this)
        }.also { gameEntity ->
            checkNotNull(gameEntity.id) { logger error "Game could not be saved" }
            val playerEntities = game.players.map { player ->
                player.copy(id = null, game = gameEntity, pieces = mutableListOf()).run {
                    playerEntityRepository.save(this)
                }.also { playerEntity ->
                    checkNotNull(playerEntity.id) { logger error "Player could not be saved" }
                    val pieceEntities = player.pieces.map { piece ->
                        piece.copy(id = null, player = playerEntity).run {
                            pieceEntityRepository.save(this)
                        }.also { pieceEntity ->
                            checkNotNull(pieceEntity.id) { logger error "Piece could not be saved" }
                        }
                    }
                    playerEntity.pieces = pieceEntities.toMutableList()
                }
            }
            gameEntity.players = playerEntities.toMutableList()
            logger debug "Game created with id: ${gameEntity.id}"
        }
    }

    override fun update(game: GameEntity) {
        gameEntityRepository.save(game)
        logger debug "Game updated with id: ${game.id}"
    }

    override fun delete(id: Long) {
        gameEntityRepository.deleteById(id)
        logger debug "Game deleted with id: $id"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameRepositoryDatabase::class.java)
    }
}
