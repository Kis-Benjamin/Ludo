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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.RoomEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.UserEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.RoomEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
class RoomRepositoryDatabase(
    private val roomEntityRepository: RoomEntityRepository,
    private val userEntityRepository: UserEntityRepository,
) : RoomRepository {
    override fun getAll(): List<RoomEntity> {
        return roomEntityRepository.findAll()
    }

    override fun get(id: Long): RoomEntity {
        return roomEntityRepository.findById(id).run {
            requireNotNull(getOrNull()) { "Room not found with id: $id" }
        }
    }

    @Transactional
    override fun insert(room: RoomEntity): RoomEntity {
        val host = requireNotNull(room.host) { "Room host is null" }
        return room.copy(id = null, host = null, users = mutableListOf()).run {
            roomEntityRepository.save(this)
        }.also {  roomEntity ->
            checkNotNull(roomEntity.id) { "Room could not be saved" }
            val userEntity = host.let { user ->
                user.copy(id = null, room = roomEntity).run {
                    userEntityRepository.save(this)
                }.also { userEntity ->
                    checkNotNull(userEntity.id) { "User could not be saved" }
                }
            }
            roomEntity.host = userEntity
            roomEntity.users = mutableListOf(userEntity)
        }
    }

    override fun update(room: RoomEntity) {
        roomEntityRepository.save(room)
    }

    override fun delete(id: Long) {
        roomEntityRepository.deleteById(id)
    }
}