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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.debug
import hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util.error
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.RoomEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.dao.UserEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.gameserver.data.model.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Repository
class UserRepositoryDatabase(
    private val roomEntityRepository: RoomEntityRepository,
    private val userEntityRepository: UserEntityRepository,
) : UserRepository {
    override fun getAll(): List<UserEntity> {
        val users = userEntityRepository.findAll()
        logger debug "Users found: ${users.size}"
        return users
    }

    override fun get(id: Long): UserEntity {
        val user = userEntityRepository.findById(id).run {
            requireNotNull(getOrNull()) { logger error "User not found with id: $id" }
        }
        logger debug "User found with id: $id"
        return user
    }

    override fun insert(user: UserEntity): UserEntity {
        val user = userEntityRepository.save(user)
        logger debug "User created, id: ${user.id }"
        return user
    }

    override fun update(user: UserEntity) {
        userEntityRepository.save(user)
        logger debug "User updated, id: ${user.id}"
    }

    @Transactional
    override fun delete(user: UserEntity) {
        val room = requireNotNull(user.room) { logger error "User must be in a room" }
        room.users.remove(user)
        logger debug "User deleted, id: ${user.id}"
        userEntityRepository.delete(user)
        roomEntityRepository.save(room)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepositoryDatabase::class.java)
    }
}