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

package hu.bme.aut.alkfejl.ludospringboot.gameserver.web.controller

import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service.ScoreService
import hu.bme.aut.alkfejl.ludospringboot.gameserver.web.model.ScoreDTO
import hu.bme.aut.alkfejl.ludospringboot.gameserver.web.model.toSessionModel
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/score")
class ScoreController(
    private val scoreService: ScoreService,
) {
    @GetMapping("/")
    fun getAll(): List<ScoreDTO> {
        val scores = scoreService.getAll()
        return scores.map { it.toSessionModel() }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ScoreController::class.java)
    }
}
