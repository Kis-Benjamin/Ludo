package hu.bme.aut.android.ludocompose.domain.model

class Player(
    var name: String = "",
    var standing: Int = 0,
    val tokens: List<Token> = List(4) { Token() },
    var actTokenIndex: Int = 0,
) {
    val isInGame: Boolean get() = tokens.any { !it.isInHome }

    val actToken: Token get() = tokens[actTokenIndex]
}
