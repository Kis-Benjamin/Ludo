package hu.bme.aut.alkfejl.ludospringboot.domain.model

import hu.bme.aut.alkfejl.ludospringboot.data.entities.GameEntity

data class Game(
    var dice: Int = 0,
    var actPlayer: Int = 0,
    val players: List<Player> = List(4) { Player() },
)

val Game.playerCount: Int get() = players.size
val Game.actPlayerRef: Player get() = players[actPlayer]
val Game.playersInGame: Int get() = players.count { it.homeCount != 0 }

fun GameEntity.toDomainModel() = Game(
    dice = dice,
    actPlayer = actPlayer,
    players = players.map { it.toDomainModel() },
)

fun GameEntity.update(game: Game) {
    dice = game.dice
    actPlayer = game.actPlayer
    players.forEachIndexed { i, player -> player.update(game.players[i]) }
}
