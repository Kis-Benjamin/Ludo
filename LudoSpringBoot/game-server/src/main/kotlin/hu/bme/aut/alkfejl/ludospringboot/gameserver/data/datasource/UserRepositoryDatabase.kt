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
        try {
            return userEntityRepository.save(user)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "User creation failed, id: ${user.id}", e)
        }
    }

    override fun update(user: UserEntity) {
        try {
            userEntityRepository.save(user)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "User update failed, id: ${user.id}", e)
        }
    }

    @Transactional
    override fun delete(user: UserEntity) {
        val room = requireNotNull(user.room) { logger error "User must be in a room" }
        try {
            room.users.remove(user)
            userEntityRepository.delete(user)
            roomEntityRepository.save(room)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(logger error "User deletion failed, id: ${user.id}", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepositoryDatabase::class.java)
    }
}