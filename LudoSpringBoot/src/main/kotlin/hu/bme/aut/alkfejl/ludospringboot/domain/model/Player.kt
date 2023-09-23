package hu.bme.aut.alkfejl.ludospringboot.domain.model

import hu.bme.aut.alkfejl.ludospringboot.data.entities.PlayerEntity

data class Player(
    var name: String = "",
    var standing: Int = 0,
    var actToken: Int = 0,
    val tokens: List<Token> = List(4) { Token() },
)

val Player.actTokenRef: Token get() = tokens[actToken]
val Player.homeCount: Int get() = tokens.count { it.state == TokenState.HOME }

fun PlayerEntity.toDomainModel() = Player(
    name = name,
    standing = standing,
    actToken = actToken,
    tokens = tokens.map { it.toDomainModel() },
)

fun PlayerEntity.update(player: Player) {
    name = player.name
    standing = player.standing
    actToken = player.actToken
    tokens.forEachIndexed { i, token -> token.update(player.tokens[i]) }
}
