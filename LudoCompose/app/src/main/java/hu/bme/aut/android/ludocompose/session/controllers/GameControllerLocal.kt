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

package hu.bme.aut.android.ludocompose.session.controllers

import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.session.converters.toSessionModel
import hu.bme.aut.android.ludocompose.session.model.GameDto
import hu.bme.aut.android.ludocompose.session.model.GameListItemDto
import kotlinx.coroutines.delay
import javax.inject.Inject

class GameControllerLocal @Inject constructor(
    private val gameService: GameService,
) : GameController {

    private var _id: Long? = null

    private val id: Long
        get() = checkNotNull(_id) { "No game loaded" }

    override val hasActive: Boolean
        get() = _id != null

    override suspend fun getActive(): GameDto {
        val game = gameService.get(id)
        return game.toSessionModel()
    }

    override suspend fun start(playerNames: List<String>) {
        require(playerNames.size in 2..4) {
            "Number of players must be between 2 and 4"
        }
        require(playerNames.all { it.isNotBlank() }) {
            "Player name must not be blank"
        }
        if (hasActive) {
            unLoad()
        }
        _id = gameService.start(playerNames)
    }

    override suspend fun getList(): List<GameListItemDto> {
        val gameListItem = gameService.getAll()
        return gameListItem.map { it.toSessionModel() }
    }

    override suspend fun load(id: Long) {
        require(id > 0) { "Id must be positive" }
        _id = gameService.save(id, null)
    }

    override suspend fun unLoad() {
        delete(id)
        _id = null
    }

    override suspend fun save(name: String) {
        require(name.isNotBlank()) { "Name must not be blank" }
        gameService.save(id, name)
    }

    override suspend fun delete(id: Long) {
        gameService.delete(id)
    }

    override suspend fun select() {
        gameService.select(id)
    }

    override suspend fun step(): Boolean {
        return gameService.step(id)
    }
}
