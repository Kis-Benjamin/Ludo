package hu.bme.aut.android.ludocompose.domain.model

import hu.bme.aut.android.ludocompose.domain.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackMultiplier
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackSize

class Game(
    playerCount: Int,
    val players: List<Player> = List(playerCount) { Player() },
    var actPlayerIndex: Int = 0,
    var dice: Int = 0,
) {
    private val playerCount: Int get() = players.size
    private val playersInGame: Int get() = players.count { it.isInGame }
    private val actPlayer: Player get() = players[actPlayerIndex]

    val winner: String get() = players.find { it.standing == 1 }!!.name

    private val isInYard get() = actPlayer.actToken.isInYard

    private val isInTrack get() = actPlayer.actToken.isInTrack

    private val isInHome get() = actPlayer.actToken.isInHome

    private val canStepInTrack get() = isInYard && dice == 6

    private val canStepOnHome get() = isInTrack &&
            actPlayer.actToken.trackPos >= trackSize + trackMultiplier * actPlayerIndex

    private val canRollAgain get() = isInTrack && dice == 6

    private val isValidStep get() = canStepInTrack || isInTrack

    private val isInGame get() = actPlayer.isInGame

    private fun nextPlayer() {
        actPlayerIndex = (actPlayerIndex + 1) % playerCount
    }

    private fun nextValidPlayer(): Boolean {
        var stepCount = 0
        do nextPlayer()
        while (!isInGame && ++stepCount < playerCount)
        return stepCount == playerCount
    }

    private fun nextToken() {
        actPlayer.actTokenIndex = (actPlayer.actTokenIndex + 1) % tokenCount
    }

    private fun nextValidToken(): Boolean {
        var stepCount = 0
        do nextToken()
        while (!isValidStep && ++stepCount < tokenCount)
        return stepCount != tokenCount
    }

    private fun executeStep() {
        val token = actPlayer.actToken
        if (isInTrack) {
            token.trackPos += dice
        }
        if (canStepInTrack) {
            token.state = Token.State.TRACK
            token.trackPos = trackMultiplier * actPlayerIndex
        }
        if (canStepOnHome) {
            token.state = Token.State.HOME
            token.trackPos = 0
            if (!isInGame)
                actPlayer.standing = playerCount - playersInGame
        }
    }

    private fun executeTokenKill() {
        if (!isInTrack) return
        for (playerIndex in players.indices) {
            val player = players[playerIndex]
            for (tokenIndex in player.tokens.indices) {
                if (actPlayerIndex == playerIndex && actPlayer.actTokenIndex == tokenIndex) continue
                val token = player.tokens[tokenIndex]
                if (token.state != Token.State.TRACK) continue
                if (token.trackPos % trackSize == actPlayer.actToken.trackPos % trackSize) {
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
        if (isInHome) nextValidToken()
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

    fun rollDice() {
        dice = set.random() % 6 + 1
    }

    companion object {
        private val set = (1..46656).shuffled()
    }

    val isSelectEnabled: Boolean get() = actPlayer.tokens.count { it.isInYard && dice == 6 || it.isInTrack } > 1

    val board: Board get() = Board.also { board ->
        board.reset()
        for (playerIndex in players.indices) {
            val player = players[playerIndex]
            var homeCount = 4
            for (tokenIndex in player.tokens.indices) {
                val token = player.tokens[tokenIndex]
                when (token.state) {
                    Token.State.YARD -> {
                        board.yardFields[playerIndex][tokenIndex].playerIndex = playerIndex
                    }
                    Token.State.TRACK -> {
                        board.trackFields[token.trackPos % trackSize].playerIndex = playerIndex
                    }
                    else -> {
                        board.homeFields[playerIndex][--homeCount].playerIndex = playerIndex
                    }
                }
            }
        }
        if (isValidStep) {
            val player = actPlayer
            val token = actPlayer.actToken
            if (token.isInYard) {
                board.yardFields[actPlayerIndex][player.actTokenIndex].isPointer = true
            } else if (token.isInTrack) {
                board.trackFields[token.trackPos % trackSize].isPointer = true
            }
        }
    }
}
