package hu.bme.aut.alkfejl.ludospringboot.domain.model

import hu.bme.aut.alkfejl.ludospringboot.data.entities.TokenEntity

data class Token(
    var state: Int = TokenState.YARD,
    var trackPos: Int = 0
)

fun TokenEntity.toDomainModel() = Token(
    state = state,
    trackPos = trackPos,
)

fun TokenEntity.update(token: Token) {
    state = token.state
    trackPos = token.trackPos
}
