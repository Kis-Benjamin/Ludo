package hu.bme.aut.android.ludocompose.domain.model

data class Board(
    val yardFields: List<List<Field>> = List(4) { List(4) { Field() } },
    val trackFields: List<Field> = List(40) { Field() },
    val homeFields: List<List<Field>> = List(4) { List(4) { Field() } }
)

val Game.board: Board get() = Board().also { board ->
    for (playerIndex in players.indices) {
        val player = players[playerIndex]
        var homeCount = 4
        for (tokenIndex in player.tokens.indices) {
            val token = player.tokens[tokenIndex]
            if (token.state == Token.State.YARD) {
                board.yardFields[playerIndex][tokenIndex].color = playerIndex
            } else if (token.state == Token.State.TRACK) {
                board.trackFields[token.trackPos % 40].color = playerIndex
            } else {
                board.homeFields[playerIndex][--homeCount].color = playerIndex
            }
        }
    }
    if (isValidStep) {
        val player = actPlayer
        val token = actPlayer.actToken
        if (isOnYard) {
            board.yardFields[actPlayerIndex][player.actTokenIndex].isPointer = true
        } else if (isOnTrack) {
            board.trackFields[token.trackPos % 40].isPointer = true
        }
    }
}
