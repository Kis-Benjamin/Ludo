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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.debug
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.ScoreEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.ScoreEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ScoreRepositoryDatabase(
    private val scoreEntityRepository: ScoreEntityRepository,
) : ScoreRepository {
    override fun getAll(): List<ScoreEntity> {
        val scores = scoreEntityRepository.findAll()
        logger debug "Scores found: ${scores.size}"
        return scores
    }

    override fun get(name: String): ScoreEntity? {
        require(name.isNotBlank()) { logger error "Name must not be blank" }
        val score = scoreEntityRepository.findByName(name)
        score?.let { logger debug "Score found with name: $name, id: ${it.id}" }
            ?: (logger debug "Score not found with name: $name")
        return score
    }

    override fun insert(item: ScoreEntity) {
        try {
            scoreEntityRepository.save(item)
            logger debug "Score created, id: ${item.id}"
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "Score creation failed, id: ${item.id}", e)
        }
    }

    override fun update(item: ScoreEntity) {
        try {
            scoreEntityRepository.save(item)
            logger debug "Score updated, id: ${item.id}"
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "Score update failed, id: ${item.id}", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ScoreRepositoryDatabase::class.java)
    }
}