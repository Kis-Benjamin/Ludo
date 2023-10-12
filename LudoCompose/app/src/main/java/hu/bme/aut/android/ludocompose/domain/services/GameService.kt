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

package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem

interface GameService {
    suspend fun has(name: String): Boolean

    suspend fun get(id: Long): Game

    suspend fun getAll(): List<GameListItem>

    suspend fun start(playerNames: List<String>): Long

    suspend fun save(id: Long, name: String)

    suspend fun delete(id: Long)

    suspend fun select(id: Long)

    suspend fun step(id: Long): Boolean
}