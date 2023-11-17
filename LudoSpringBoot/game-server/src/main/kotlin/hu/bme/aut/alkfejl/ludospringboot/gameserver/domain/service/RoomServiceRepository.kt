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
        require(id > 0) { "Id must be positive" }
        val room = roomRepository.get(id)
        return room.toDomainModel()
    }

    override fun getUsers(id: Long): List<User> {
        require(id > 0) { "Id must be positive" }
        val room = roomRepository.get(id)
        return room.users.map { it.toDomainModel() }
    }

    override fun delete(id: Long) {
        require(id > 0) { "Id must be positive" }
        roomRepository.delete(id)
    }

    override fun create(name: String, hostName: String, hostId: String): Long {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(hostName.isNotBlank()) { "Host name must not be blank" }
        require(hostId.isNotBlank()) { "Host id must not be blank" }
        val room = RoomEntity(name, hostName, hostId).run {
            roomRepository.insert(this)
        }
        return room.id!!
    }

    override fun close(id: Long, userId: String) {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host!!.subject == userId) { "Only this room's host can close it" }
        roomRepository.delete(id)
    }

    override fun join(id: Long, userName: String, userId: String): Long {
        require(id > 0) { "Id must be positive" }
        require(userName.isNotBlank()) { "User name must not be blank" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host!!.subject != userId) { "This room's host cannot join again" }
        require(room.users.none { it.subject == userId }) { "This user is already in this room" }
        val userEntity = UserEntity(
            room = room,
            name = userName,
            subject = userId,
        ).apply {
            userRepository.insert(this)
        }
        return userEntity.id!!
    }

    override fun leave(id: Long, userId: String) {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { "This room's host cannot leave" }
        val userEntity = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { "This user is not in this room" }
        }
        userRepository.delete(userEntity)
    }

    override fun ready(id: Long, userId: String) {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { "This room's host cannot ready" }
        val user = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { "This user is not in this room" }
        }
        user.isReady = true
        userRepository.update(user)
    }

    override fun unready(id: Long, userId: String) {
        require(id > 0) { "Id must be positive" }
        require(userId.isNotBlank()) { "User id must not be blank" }
        val room = roomRepository.get(id)
        require(room.host?.subject != userId) { "This room's host cannot unready" }
        val user = room.users.find { it.subject == userId }.run {
            requireNotNull(this) { "This user is not in this room" }
        }
        user.ready = false
        userRepository.update(user)
    }
}