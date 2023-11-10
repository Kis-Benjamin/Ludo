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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service.GameService
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service.ScoreServiceRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.web.model.*
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/game")
class GameController(
    private val gameService: GameService,
    private val scoreService: ScoreServiceRepository,
    private val template: SimpMessagingTemplate,
) {
    @GetMapping("/{id}")
    fun getBoard(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ): BoardDTO? {
        val userId = principal.subject
        val board = gameService.getBoard(id, userId)
        return board.toSessionModel()
    }

    @PostMapping("/{id}/select")
    fun select(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId = principal.subject
        gameService.select(id, userId)
        template.convertAndSend("/game/update", GameUpdate(id))
    }

    @PostMapping("/{id}/step")
    fun step(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId = principal.subject
        val ended = gameService.step(id, userId)
        if (!ended) {
            template.convertAndSend("/game/update", GameUpdate(id))
        } else {
            val winnerName = gameService.getWinner(id)
            scoreService.addOrIncrement(winnerName)
            gameService.delete(id)
            template.convertAndSend("/game/end", GameEnd(id))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameController::class.java)
    }

//    @PostMapping("/hello/{name}")
//    fun sendMessage(@PathVariable name: String) {
//        template.convertAndSend("/game/hello", HelloMessage(name))
//    }
//
//    data class HelloMessage(val name: String)
}
