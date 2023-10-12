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

package hu.bme.aut.android.ludocompose.session.controllers

import hu.bme.aut.android.ludocompose.domain.services.ScoreService
import hu.bme.aut.android.ludocompose.session.converters.toSessionModel
import hu.bme.aut.android.ludocompose.session.model.ScoreItemDto
import javax.inject.Inject

class ScoreControllerLocal @Inject constructor(
    private val scoreService: ScoreService,
) : ScoreController {
    override suspend fun getAll(): List<ScoreItemDto> {
        val score = scoreService.getAll()
        return score.map { it.toSessionModel() }
    }

    override suspend fun save(name: String) {
        scoreService.save(name)
    }

    override suspend fun delete(id: Long) {
        scoreService.delete(id)
    }
}
