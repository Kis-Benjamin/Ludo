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

package hu.bme.aut.android.ludocompose.session.controller

import hu.bme.aut.android.ludocompose.session.model.RoomDTO

interface RoomController {
    val hasActive: Boolean

    val id: Long

    suspend fun load(id: Long)

    suspend fun unLoad()

    suspend fun getAll(): List<RoomDTO>

    suspend fun get(id: Long): RoomDTO

    suspend fun get(): RoomDTO

    suspend fun create(name: String)

    suspend fun start()

    suspend fun close()

    suspend fun join(id: Long)

    suspend fun leave()

    suspend fun ready()

    suspend fun unready()
}