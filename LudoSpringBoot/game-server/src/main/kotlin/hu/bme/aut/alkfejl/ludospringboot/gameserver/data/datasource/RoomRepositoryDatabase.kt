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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.debug
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.RoomEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.UserEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.RoomEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
class RoomRepositoryDatabase(
    private val roomEntityRepository: RoomEntityRepository,
    private val userEntityRepository: UserEntityRepository,
) : RoomRepository {
    override fun getAll(): List<RoomEntity> {
        val rooms = roomEntityRepository.findAll()
        logger debug "Rooms found: ${rooms.size}"
        return rooms
    }

    override fun get(id: Long): RoomEntity {
        val room = roomEntityRepository.findById(id).run {
            requireNotNull(getOrNull()) { logger error "Room not found with id: $id" }
        }
        logger debug "Room found with id: $id"
        return room
    }

    @Transactional
    override fun insert(room: RoomEntity): RoomEntity {
        val host = requireNotNull(room.host) { logger error "Room host is null" }
        try {
            return room.copy(id = null, host = null, users = mutableListOf()).run {
                roomEntityRepository.save(this)
            }.also { roomEntity ->
                checkNotNull(roomEntity.id) { logger error "Room creation failed" }
                val userEntity = host.let { user ->
                    user.copy(id = null, room = roomEntity).run {
                        userEntityRepository.save(this)
                    }.also { userEntity ->
                        checkNotNull(userEntity.id) { logger error "User creation failed" }
                    }
                }
                roomEntity.host = userEntity
                roomEntity.users = mutableListOf(userEntity)
                logger debug "Room created with id: ${roomEntity.id}"
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "Room creation failed", e)
        }
    }

    override fun update(room: RoomEntity) {
        try {
            roomEntityRepository.save(room)
            logger debug "Room updated with id: ${room.id}"
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "Room update failed", e)
        }
    }

    override fun delete(id: Long) {
        try {
            roomEntityRepository.deleteById(id)
            logger debug "Room deleted with id: $id"
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "Room deletion failed", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RoomRepositoryDatabase::class.java)
    }
}