package hu.bme.aut.android.ludocompose.domain.model

data class Player(
    var name: String = "",
    var standing: Int = 0,
    var homeCount: Int = 4,
    val tokens: List<Token> = List(4) { Token() },
    var actTokenIndex: Int = 0,
)

val Player.actToken: Token get() = tokens[actTokenIndex]

fun Player.finish() {
    homeCount = 0
}
