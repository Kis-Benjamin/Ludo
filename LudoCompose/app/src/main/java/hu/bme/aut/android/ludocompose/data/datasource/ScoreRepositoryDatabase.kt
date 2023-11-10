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

import hu.bme.aut.android.ludocompose.data.dao.ScoreDao
import hu.bme.aut.android.ludocompose.data.model.ScoreEntity
import javax.inject.Inject

class ScoreRepositoryDatabase @Inject constructor(
    private val scoreDao: ScoreDao
) : ScoreRepository {
    override suspend fun getAll() = scoreDao.getAll()

    override suspend fun get(name: String): ScoreEntity? {
        require(name.isNotBlank()) { "Name must not be blank" }
        return scoreDao.get(name)
    }

    override suspend fun insert(item: ScoreEntity) {
        require(item.id == null) { "Id must be null" }
        scoreDao.insert(item)
    }

    override suspend fun update(item: ScoreEntity) {
        require(item.id != null) { "Id must not be null" }
        scoreDao.update(item)
    }
}
