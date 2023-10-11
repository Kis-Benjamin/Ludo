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

package hu.bme.aut.android.ludocompose.domain.converters

import hu.bme.aut.android.ludocompose.data.model.GameEntity
import hu.bme.aut.android.ludocompose.data.model.PlayerEntity
import hu.bme.aut.android.ludocompose.data.model.TokenEntity
import hu.bme.aut.android.ludocompose.data.model.GameWithPlayers
import hu.bme.aut.android.ludocompose.data.model.PlayerWithTokens
import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import hu.bme.aut.android.ludocompose.domain.model.Player
import hu.bme.aut.android.ludocompose.domain.model.Token
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

private fun Token.toDataModel(tokenIndex: Int) = TokenEntity(
    index = tokenIndex,
    state = state.ordinal,
    trackPos = trackPos,
)

private fun Player.toDataModel(playerIndex: Int) = PlayerWithTokens(
    player = PlayerEntity(
        index = playerIndex,
        name = name,
        standing = standing,
        actToken = actTokenIndex,
    ),
    tokens = tokens.mapIndexed { tokenIndex, token -> token.toDataModel(tokenIndex) }
)

fun Game.toDataModel(name: String) = GameWithPlayers(
    game = GameEntity(
        name = name.also { require(it.isNotBlank()) { "Name must not be blank" } },
        date = LocalDateTime.now().run {
            LocalDate(year, monthValue, dayOfMonth)
        },
        dice = dice,
        actPlayer = actPlayerIndex,
    ),
    players = players.mapIndexed { playerIndex, player -> player.toDataModel(playerIndex) }
)

private fun TokenEntity.toDomainModel(): Token {
    require(id != null) { "Token id must not be null" }
    return Token(
        state = Token.State.entries[state],
        trackPos = trackPos,
    )
}

private fun PlayerWithTokens.toDomainModel(): Player {
    require(player.id != null) { "Player id must not be null" }
    return Player(
        name = player.name,
        standing = player.standing,
        actTokenIndex = player.actToken,
        tokens = tokens
            .sortedBy { it.index }
            .map {
                it.toDomainModel()
            },
    )
}

fun GameWithPlayers.toDomainModel(): Game {
    require(game.id != null) { "Game id must not be null" }
    return Game(
        dice = game.dice,
        actPlayerIndex = game.actPlayer,
        players = players
            .sortedBy { it.player.index }
            .map {
                it.toDomainModel()
            },
    )
}

fun GameEntity.toDomainListModel(): GameListItem {
    require(id != null) { "Game id must not be null" }
    return GameListItem(
        id = id,
        name = name,
        date = date,
    )
}

fun GameWithPlayers.update(name: String) =
    copy(
        game = this.game.copy(name = name.also {
            require(it.isNotBlank()) { "Name must not be blank" }
        }),
        players = players,
    )

private fun TokenEntity.update(token: Token) =
    copy(
        state = token.state.ordinal,
        trackPos = token.trackPos,
    )

private fun PlayerEntity.update(player: Player) =
    copy(
        name = player.name,
        standing = player.standing,
        actToken = player.actTokenIndex,
    )

private fun GameEntity.update(game: Game) =
    copy(
        dice = game.dice,
        actPlayer = game.actPlayerIndex,
    )

private fun PlayerWithTokens.update(player: Player) =
    copy(
        player = this.player.update(player),
        tokens = tokens.map { it.update(player.tokens[it.index]) },
    )

fun GameWithPlayers.update(game: Game) =
    copy(
        game = this.game.update(game),
        players = players.map { it.update(game.players[it.player.index]) },
    )
