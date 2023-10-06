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

import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDataModel
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import hu.bme.aut.android.ludocompose.domain.model.ScoreItem
import javax.inject.Inject

class ScoreServiceLocal @Inject constructor(
    private val scoreRepository: ScoreRepository,
) : ScoreService {
    override suspend fun getAll(): List<ScoreItem> {
        val scoreEntities = scoreRepository.getAll()
        return scoreEntities.map { it.toDomainModel() }
    }

    override suspend fun save(name: String) {
        val scoreEntity = scoreRepository.get(name)?.let {
            it.copy(winCount = it.winCount + 1)
        }
        if (scoreEntity != null) {
            scoreRepository.update(scoreEntity)
        } else {
            val scoreEntity = ScoreItem(name = name, winCount = 1).toDataModel()
            scoreRepository.insert(scoreEntity)
        }
    }

    override suspend fun delete(id: Long) {
        scoreRepository.delete(id)
    }
}