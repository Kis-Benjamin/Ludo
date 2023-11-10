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

import hu.bme.aut.alkfejl.ludospringboot.gameserver.domain.model.Board

interface GameService {
    fun start(userDetails: List<Pair<String, String>>): Long

    fun delete(id: Long)

    fun getBoard(id: Long, userId: String): Board

    fun select(id: Long, userId: String)

    fun step(id: Long, userId: String): Boolean

    fun getWinner(id: Long): String
}