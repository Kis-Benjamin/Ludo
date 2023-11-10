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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.ScoreEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.ScoreEntity
import org.springframework.stereotype.Repository

@Repository
class ScoreRepositoryDatabase(
    private val scoreEntityRepository: ScoreEntityRepository,
) : ScoreRepository {
    override fun getAll(): List<ScoreEntity> {
        return scoreEntityRepository.findAll()
    }

    override fun get(name: String): ScoreEntity? {
        return scoreEntityRepository.findByName(name)
    }

    override fun insert(item: ScoreEntity) {
        scoreEntityRepository.save(item)
    }

    override fun update(item: ScoreEntity) {
        scoreEntityRepository.save(item)
    }
}