package hu.bme.aut.android.ludocompose.domain.model

class Player(
    var name: String = "",
    var standing: Int = 0,
    val tokens: List<Token> = List(4) { Token() },
    var actTokenIndex: Int = 0,
) {
    val isInGame: Boolean get() = tokens.any { !it.isInHome }

    val actToken: Token get() = tokens[actTokenIndex]

    val isInYard: Boolean get() = actToken.isInYard

    val isInTrack: Boolean get() = actToken.isInTrack

    val isInHome: Boolean get() = actToken.isInHome


    fun isValidStep(dice: Int) = tokens.any { it.isValidStep(dice) }

    fun nextToken() {
        actTokenIndex = (actTokenIndex + 1) % 4
    }

    fun nextValidToken(dice: Int): Boolean {
        var stepCount = 0
        do nextToken()
        while (!actToken.isValidStep(dice) && ++stepCount < 4)
        return stepCount != 4
    }

    fun executeStep(color: Int, dice: Int): Boolean {
        val enteredHome = actToken.executeStep(color, dice)
        if (enteredHome) {
            nextValidToken(dice)
        }
        return enteredHome && !isInGame
    }
}
