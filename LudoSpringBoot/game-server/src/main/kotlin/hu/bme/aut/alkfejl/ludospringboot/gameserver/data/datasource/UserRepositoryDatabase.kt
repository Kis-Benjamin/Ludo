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
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.UserEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
class UserRepositoryDatabase(
    private val roomEntityRepository: RoomEntityRepository,
    private val userEntityRepository: UserEntityRepository,
) : UserRepository {
    override fun getAll(): List<UserEntity> {
        return userEntityRepository.findAll()
    }

    override fun get(id: Long): UserEntity {
        return userEntityRepository.findById(id).run {
            requireNotNull(getOrNull()) { "User not found with id: $id" }
        }
    }

    override fun insert(user: UserEntity): UserEntity {
        return userEntityRepository.save(user)
    }

    override fun update(user: UserEntity) {
        userEntityRepository.save(user)
    }

    @Transactional
    override fun delete(user: UserEntity) {
        val room = requireNotNull(user.room) { "User must be in a room" }
        room.users.remove(user)
        userEntityRepository.delete(user)
        roomEntityRepository.save(room)
    }
}