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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Room
import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.User

interface RoomService {
    fun getAll(): List<Room>

    fun get(id: Long): Room

    fun getUsers(id: Long): List<User>

    fun delete(id: Long)

    fun create(name: String, hostName: String, hostId: String): Long

    fun close(id: Long, userId: String)

    fun join(id: Long, userName: String, userId: String): Long

    fun leave(id: Long, userId: String)

    fun ready(id: Long, userId: String)

    fun unready(id: Long, userId: String)
}