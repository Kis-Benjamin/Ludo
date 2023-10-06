package hu.bme.aut.android.ludocompose.domain.converters

import hu.bme.aut.android.ludocompose.data.entities.GameEntity
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity
import hu.bme.aut.android.ludocompose.data.entities.TokenEntity
import hu.bme.aut.android.ludocompose.data.pojos.GameWithPlayers
import hu.bme.aut.android.ludocompose.data.pojos.PlayerWithTokens
import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import hu.bme.aut.android.ludocompose.domain.model.Player
import hu.bme.aut.android.ludocompose.domain.model.Token
import kotlinx.datetime.LocalDate

fun Game.toDataModel(name: String, date: LocalDate) = GameWithPlayers(
    game = GameEntity(
        name = name,
        date = date,
        dice = dice,
        actPlayer = actPlayerIndex,
    ),
    players = players.mapIndexed { playerIndex, player -> player.toDataModel(playerIndex) }
)

fun Player.toDataModel(playerIndex: Int) = PlayerWithTokens(
    player = PlayerEntity(
        index = playerIndex,
        name = name,
        standing = standing,
        actToken = actTokenIndex,
    ),
    tokens = tokens.mapIndexed { tokenIndex, token -> token.toDataModel(tokenIndex) }
)

fun Token.toDataModel(tokenIndex: Int) = TokenEntity(
    index = tokenIndex,
    state = state,
    trackPos = trackPos,
)

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

fun PlayerWithTokens.toDomainModel(): Player {
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

fun TokenEntity.toDomainModel(): Token {
    require(id != null) { "Token id must not be null" }
    return Token(
        state = state,
        trackPos = trackPos,
    )
}

fun GameEntity.toDomainModel(): GameListItem {
    require(id != null) { "Game id must not be null" }
    return GameListItem(
        id = id,
        name = name,
        date = date,
    )
}
