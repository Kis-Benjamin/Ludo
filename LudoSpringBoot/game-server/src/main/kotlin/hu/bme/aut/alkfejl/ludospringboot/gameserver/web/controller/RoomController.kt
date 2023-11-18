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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.model.Constants.playerMinCount
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.info
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service.GameService
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.service.RoomService
import hu.bme.aut.alkfejl.ludospringboot.gameserver.web.model.*
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/room")
class RoomController(
    private val roomService: RoomService,
    private val gameService: GameService,
    private val template: SimpMessagingTemplate,
) {
    @GetMapping("/")
    fun getAll(): List<RoomDTO> {
        val rooms = roomService.getAll()
        return rooms.map { it.toSessionModel() }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): RoomDTO {
        val room = roomService.get(id)
        return room.toSessionModel()
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('SCOPE_ludo.host')")
    fun create(
        @RequestBody room: RoomRequest,
        @AuthenticationPrincipal principal: Jwt,
    ): Long {
        require(principal.claims.containsKey("name")) {
            logger.error("No name present in access token")
            "Name must be present in token, did you forget to request the profile scope?"
        }
        val hostName = principal.claims["name"] as String
        val hostId = principal.subject
        val id = roomService.create(room.name, hostName, hostId)
        template.convertAndSend("/room/create", RoomCreate(id))
        return id
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ludo.host')")
    fun start(@PathVariable id: Long) {
        val users = roomService.getUsers(id)
        val userDetails = users.map { it.name to it.subject }
        require(userDetails.size >= playerMinCount) {
            logger info "Not enough players in room"
        }
        require(users.all { it.ready }) {
            logger info "Not all players are ready"
        }
        val gameId = gameService.start(userDetails)
        roomService.delete(id)
        template.convertAndSend("/room/start", RoomStart(id, gameId))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ludo.host')")
    fun close(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId: String = principal.subject
        roomService.close(id, userId)
        template.convertAndSend("/room/close", RoomClose(id))
    }

    @PostMapping("/{id}/join")
    fun join(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        require(principal.claims.containsKey("name")) {
            logger.error("No name present in access token")
            "Name must be present in token, did you forget to request the profile scope?"
        }
        val userName = principal.claims["name"] as String
        val userId = principal.subject
        roomService.join(id, userName, userId)
        template.convertAndSend("/room/update", RoomUpdate(id))
    }

    @PostMapping("/{id}/leave")
    fun leave(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId = principal.subject
        roomService.leave(id, userId)
        template.convertAndSend("/room/update", RoomUpdate(id))
    }

    @PostMapping("/{id}/ready")
    fun ready(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId: String = principal.subject
        roomService.ready(id, userId)
        template.convertAndSend("/room/update", RoomUpdate(id))
    }

    @PostMapping("/{id}/unready")
    fun unready(
        @PathVariable id: Long,
        @AuthenticationPrincipal principal: Jwt,
    ) {
        val userId: String = principal.subject
        roomService.unready(id, userId)
        template.convertAndSend("/room/update", RoomUpdate(id))
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RoomController::class.java)
    }
}