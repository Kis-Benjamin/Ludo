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

package hu.bme.aut.android.ludocompose.session.manager

import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.domain.services.ScoreService
import hu.bme.aut.android.ludocompose.session.model.BoardDTO
import hu.bme.aut.android.ludocompose.session.model.GameDTO
import hu.bme.aut.android.ludocompose.session.model.toSessionModel
import javax.inject.Inject

class GameManagerLocal @Inject constructor(
    private val gameService: GameService,
    private val scoreService: ScoreService,
) : GameManager {

    private var mId: Long? = null

    override val id: Long
        get() = checkNotNull(mId) { "No game loaded" }

    override val hasActive get() = mId != null

    override suspend fun load(id: Long) {
        require(id > 0) { "Id must be positive" }
        if (hasActive) {
            unLoad()
        }
        mId = gameService.save(id, null)
    }

    override suspend fun unLoad() {
        delete(id)
        mId = null
    }

    override suspend fun getBoard(): BoardDTO {
        val board = gameService.getBoard(id)
        return board.toSessionModel()
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
        mId = gameService.start(playerNames)
    }

    override suspend fun getList(): List<GameDTO> {
        val games = gameService.getAll()
        return games.map { it.toSessionModel() }
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

    override suspend fun step() {
        val ended = gameService.step(id)
        if (ended) {
            val winner = gameService.getWinner(id)
            scoreService.addOrIncrement(winner)
        }
    }

    override suspend fun isFinished(): Boolean {
        return gameService.isFinished(id)
    }
}
