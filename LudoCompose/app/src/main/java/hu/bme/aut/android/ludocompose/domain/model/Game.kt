package hu.bme.aut.android.ludocompose.domain.model

data class Game(
    var playerCount: Int,
    var playersInGame: Int = playerCount,
    val players: List<Player> = List(playerCount) { Player() },
    var actPlayerIndex: Int = 0,
    var dice: Int = 0,
)

val Game.actPlayer: Player get() = players[actPlayerIndex]
