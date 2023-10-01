package hu.bme.aut.android.ludocompose.domain.model


private fun Game.isOnYard() =
    actPlayer.actToken.state == Token.State.YARD

private fun Game.isOnTrack() =
    actPlayer.actToken.state == Token.State.TRACK

private fun Game.isInHome() =
    actPlayer.actToken.state == Token.State.HOME

private fun Game.canStepInTrack() =
    isOnYard() && dice == 6

private fun Game.canStepOnHome() =
    isOnTrack() && actPlayer.actToken.trackPos >= 40 + 10 * actPlayerIndex

private fun Game.canRollAgain() =
    isOnTrack() && dice == 6

private fun Game.isValidStep() =
    canStepInTrack() || isOnTrack()

private fun Game.isInGame() =
    actPlayer.homeCount != 0

private fun Game.nextPlayer() {
    actPlayerIndex = (actPlayerIndex + 1) % playerCount
}

private fun Game.nextValidPlayer(): Boolean {
    var stepCount = 0
    do nextPlayer()
    while (!isInGame() && ++stepCount < playerCount)
    return stepCount == playerCount
}

private fun Game.nextToken() {
    actPlayer.actTokenIndex = (actPlayer.actTokenIndex + 1) % 4
}

private fun Game.nextValidToken(): Boolean {
    var stepCount = 0
    do nextToken()
    while (!isValidStep() && ++stepCount < 4)
    return stepCount != 4
}

private fun Game.validTokenCount(): Int {
    var validTokenCount = 0
    for (i in 0..3) {
        nextToken()
        if (isValidStep()) validTokenCount++
    }
    return validTokenCount
}

private fun Game.executeStep() {
    val token = actPlayer.actToken
    if (isOnTrack()) {
        token.trackPos += dice
    }
    if (canStepInTrack()) {
        token.state = Token.State.TRACK
        token.trackPos = 10 * actPlayerIndex
    }
    if (canStepOnHome()) {
        token.state = Token.State.HOME
        token.trackPos = 0
        actPlayer.homeCount--
        if (!isInGame())
            actPlayer.standing = playerCount - --playersInGame
    }
}

private fun Game.executeTokenKill() {
    if (!isOnTrack()) return
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

fun Game.rollDice() {
    dice = (1..46656).shuffled().random() % 6 + 1
}

fun Game.updateGameBoard() {
    board.clearState()
    for (playerIndex in players.indices) {
        val player = players[playerIndex]
        var homeCount = 4
        for (tokenIndex in player.tokens.indices) {
            val token = player.tokens[tokenIndex]
            if (token.state == Token.State.YARD) {
                board.yardFields[playerIndex][tokenIndex].playerIndex = playerIndex
            } else if (token.state == Token.State.TRACK) {
                board.trackFields[token.trackPos % 40].playerIndex = playerIndex
            } else {
                board.homeFields[playerIndex][--homeCount].playerIndex = playerIndex
            }
        }
    }
    if (isValidStep()) {
        val player = actPlayer
        val token = actPlayer.actToken
        if (isOnYard()) {
            board.yardFields[actPlayerIndex][player.actTokenIndex].isPointer = true
        } else if (isOnTrack()) {
            board.trackFields[token.trackPos % 40].isPointer = true
        }
    }
}

val Game.isSelectEnabled: Boolean get() = validTokenCount() > 1

fun Game.select() {
    nextValidToken()
    updateGameBoard()
}

fun Game.step(): Boolean {
    executeStep()
    if (isInHome()) nextValidToken()
    executeTokenKill()
    if (canRollAgain()) rollDice()
    else {
        if (nextValidPlayer()) {
            updateGameBoard()
            return true
        } else {
            rollDice()
            nextValidToken()
        }
    }
    updateGameBoard()
    return false
}

fun Game.getWinner(): String =
    players.find { it.standing == 1 }!!.name
