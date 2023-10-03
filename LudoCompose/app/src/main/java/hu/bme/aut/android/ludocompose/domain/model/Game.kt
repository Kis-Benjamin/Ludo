package hu.bme.aut.android.ludocompose.domain.model

import hu.bme.aut.android.ludocompose.domain.util.circular

class Game(
    playerCount: Int,
    val players: List<Player> = List(playerCount) { Player() },
    var actPlayerIndex: Int = 0,
    var dice: Int = 0,
) {
    val playerCount: Int get() = players.size
    val playersInGame: Int get() = players.count { it.isInGame }
    val actPlayer: Player get() = players[actPlayerIndex]

    val isOnYard: Boolean get() = actPlayer.isInYard

    val isOnTrack: Boolean get() = actPlayer.isInTrack

    val isInHome: Boolean get() = actPlayer.isInHome

    val isValidStep: Boolean get() = actPlayer.isValidStep(dice)

    val canRollAgain: Boolean get() = isValidStep && dice == 6

    val winner: String get() = players.find { it.standing == 1 }!!.name

    fun nextPlayer() {
        actPlayerIndex = (actPlayerIndex + 1) % playerCount
    }

    fun nextValidPlayer(): Boolean {
        var stepCount = 0
        do nextPlayer()
        while (!actPlayer.isInGame && ++stepCount < playerCount)
        return stepCount == playerCount
    }

    fun nextValidToken() {
        actPlayer.nextValidToken(dice)
    }

    fun rollDice() {
        dice = set.random() % 6 + 1
    }

    fun executeStep() {
        val finished = actPlayer.executeStep(actPlayerIndex, dice)
        if (finished) {
            actPlayer.standing = playerCount - playersInGame
        }
    }

    fun executeTokenKill() {
        if (!isOnTrack) return
        for (playerIndex in players.indices) {
            val player = players[playerIndex]
            for (tokenIndex in player.tokens.indices) {
                if (actPlayerIndex == playerIndex && actPlayer.actTokenIndex == tokenIndex) continue
                val token = player.tokens[tokenIndex]
                if (token.state != Token.State.TRACK) continue
                if (token.trackPos % 40 == actPlayer.actToken.trackPos % 40) {
                    token.trackPos = 0
                    token.state = Token.State.YARD
                    return
                }
            }
        }
    }

    fun select() {
        nextValidToken()
    }

    fun step(): Boolean {
        executeStep()
        executeTokenKill()
        if (canRollAgain) rollDice()
        else {
            if (nextValidPlayer()) {
                return true
            } else {
                rollDice()
                nextValidToken()
            }
        }
        return false
    }

    companion object {
        private val set = (1..46656).shuffled()
    }
}
