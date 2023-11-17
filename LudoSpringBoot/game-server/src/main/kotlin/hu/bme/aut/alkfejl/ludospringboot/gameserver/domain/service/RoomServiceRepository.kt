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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.info
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.warn
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.datasource.RoomRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.datasource.UserRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.RoomEntity
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.UserEntity
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Room
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.User
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.toDomainModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoomServiceRepository(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
) : RoomService {
    override fun getAll(): List<Room> {
        val rooms = roomRepository.getAll()
        return rooms.map { it.toDomainModel() }
    }

    override fun get(id: Long): Room {
        val room = roomRepository.get(id)
        return room.toDomainModel()
    }

    override fun getUsers(id: Long): List<User> {
        val room = roomRepository.get(id)
        return room.users.map { it.toDomainModel() }
    }

    override fun delete(id: Long) {
        roomRepository.delete(id)
    }

    override fun create(name: String, hostName: String, hostId: String): Long {
        require(name.isNotBlank()) { logger error "Name must not be blank" }
        require(hostName.isNotBlank()) { logger error "Host name must not be blank" }
        require(hostId.isNotBlank()) { logger error "Host id must not be blank" }
        val room = RoomEntity(name, hostName, hostId).run {
            roomRepository.insert(this)
        }
        val id = room.id!!
        logger info "Room created with id: $id"
        return id
    }

    override fun close(id: Long, userId: String) {
        require(userId.isNotBlank()) { logger error "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host!!.subject == userId) { logger error "Only the host can close the room" }
        logger info "Room closed by host with id: $id"
        roomRepository.delete(id)
    }

    override fun join(id: Long, userName: String, userId: String): Long {
        require(userName.isNotBlank()) { logger error "User name must not be blank" }
        require(userId.isNotBlank()) { logger error "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host!!.subject != userId) { logger error "The host should not join" }
        require(room.users.none { it.subject == userId }) { logger warn "The user is present in this room" }
        val userEntity = UserEntity(
            room = room,
            name = userName,
            subject = userId,
        ).apply {
            userRepository.insert(this)
        }
        val userEntityId = userEntity.id!!
        logger info "User '$userName' joined to Room with id: $id"
        return userEntityId
    }

    override fun leave(id: Long, userId: String) {
        require(userId.isNotBlank()) { logger error "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { logger error "The host should not leave" }
        val userEntity = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { logger error "This user is not in this room" }
        }
        logger info "User '${userEntity.name}' left from Room with id: $id"
        userRepository.delete(userEntity)
    }

    override fun ready(id: Long, userId: String) {
        require(userId.isNotBlank()) { logger error "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { logger error "This room's host cannot ready" }
        val user = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { logger error "This user is not in this room" }
        }
        user.ready = true
        logger info "User '${user.name}' is ready in Room with id: $id"
        userRepository.update(user)
    }

    override fun unready(id: Long, userId: String) {
        require(userId.isNotBlank()) { logger error "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { logger error "This room's host cannot unready" }
        val user = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { logger error "This user is not in this room" }
        }
        user.ready = false
        logger info "User '${user.name}' is not ready in Room with id: $id"
        userRepository.update(user)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RoomServiceRepository::class.java)
    }
}