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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.datasource.ScoreRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.ScoreEntity
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Score
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.toDomainModel
import org.springframework.stereotype.Service

@Service
class ScoreServiceRepository(
    private val scoreRepository: ScoreRepository,
) : ScoreService {
    override fun getAll(): List<Score> {
        val scoreEntities = scoreRepository.getAll()
        return scoreEntities.map { it.toDomainModel() }
    }

    override fun addOrIncrement(name: String) {
        require(name.isNotBlank()) { "Name must not be blank" }
        scoreRepository.get(name)?.let { score ->
            score.copy(winCount = score.winCount + 1)
        }?.apply {
            scoreRepository.update(this)
        } ?: run {
            val score = ScoreEntity(name = name, winCount = 1)
            scoreRepository.insert(score)
        }
    }
}
