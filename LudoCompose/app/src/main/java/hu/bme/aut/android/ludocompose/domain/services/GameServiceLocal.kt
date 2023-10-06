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

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDataModel
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class GameServiceLocal @Inject constructor(
    private val gameRepository: GameRepository,
) : GameService {

    private var game: Game? = null

    override val hasActive: Boolean
        get() = game != null

    override suspend fun getActive(): Game {
        var i = 0
        while (++i <= 5 && game == null) {
            delay(1000)
        }
        return checkNotNull(game) { "No game loaded" }
    }

    override suspend fun start(playerNames: List<String>) {
        val game = Game(playerNames)
        game.rollDice()
        this.game = game
    }

    override suspend fun getList(): List<GameListItem> {
        val gameEntities = gameRepository.getAll()
        return gameEntities.map { it.toDomainModel() }
    }

    override suspend fun load(id: Long) {
        val gameEntity = gameRepository.get(id)
        game = gameEntity.toDomainModel()
    }

    override suspend fun unLoad() {
        game = null
    }

    override suspend fun save(name: String) {
        gameRepository.get(name)?.let {
            throw IllegalArgumentException("Game already exists")
        }
        val date = LocalDateTime.now()
        val localDate = LocalDate(
            date.year,
            date.monthValue,
            date.dayOfMonth,
        )
        val game = checkNotNull(game) { "No game loaded" }
        val gameEntity = game.toDataModel(name, localDate)
        gameRepository.insert(gameEntity)
    }

    override suspend fun delete(id: Long) {
        gameRepository.delete(id)
    }

    override suspend fun select() {
        val game = checkNotNull(game) { "No game loaded" }
        game.select()
    }

    override suspend fun step(): Boolean {
        val game = checkNotNull(game) { "No game loaded" }
        return game.step()
    }
}