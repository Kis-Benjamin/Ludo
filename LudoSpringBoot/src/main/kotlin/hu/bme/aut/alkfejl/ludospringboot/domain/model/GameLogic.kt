package hu.bme.aut.alkfejl.ludospringboot.domain.model


private fun Game.isOnYard() =
    actPlayerRef.actTokenRef.state == TokenState.YARD

private fun Game.isOnTrack() =
    actPlayerRef.actTokenRef.state == TokenState.TRACK

private fun Game.isInHome() =
    actPlayerRef.actTokenRef.state == TokenState.HOME

private fun Game.canStepInTrack() =
    isOnYard() && dice == 6

private fun Game.canStepOnHome() =
    isOnTrack() && actPlayerRef.actTokenRef.trackPos >= 40 + 10 * actPlayer

private fun Game.canRollAgain() =
    isOnTrack() && dice == 6

private fun Game.isValidStep() =
    canStepInTrack() || isOnTrack()

private fun Game.isInGame() =
    actPlayerRef.homeCount != 0

private fun Game.nextPlayer() {
    actPlayer = (actPlayer + 1) % playerCount
}

private fun Game.nextValidPlayer(): Boolean {
    var stepCount = 0
    do nextPlayer()
    while (!isInGame() && ++stepCount < playerCount)
    return stepCount == playerCount
}

private fun Game.nextToken() {
    actPlayerRef.actToken = (actPlayerRef.actToken + 1) % 4
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
    val token = actPlayerRef.actTokenRef
    if (isOnTrack()) {
        token.trackPos += dice
    }
    if (canStepInTrack()) {
        token.state = TokenState.TRACK
        token.trackPos = 10 * actPlayer
    }
    if (canStepOnHome()) {
        token.state = TokenState.HOME
        token.trackPos = 0
        if (!isInGame())
            actPlayerRef.standing = playerCount - playersInGame
    }
}

private fun Game.executeTokenKill() {
    if (!isOnTrack()) return
    for (playerIndex in players.indices) {
        val player = players[playerIndex]
        for (tokenIndex in player.tokens.indices) {
            if (actPlayer == playerIndex && actPlayerRef.actToken == tokenIndex) continue
            val token = player.tokens[tokenIndex]
            if (token.state != TokenState.TRACK) continue
            if (token.trackPos % 40 == actPlayerRef.actTokenRef.trackPos % 40) {
                token.trackPos = 0
                token.state = TokenState.YARD
                return
            }
        }
    }
}

fun Game.rollDice() {
    dice = (1..46656).shuffled().random() % 6 + 1
}

fun Game.select(name: String) {
    if (actPlayerRef.name != name) return
    nextValidToken()
}

fun Game.step(name: String): Boolean {
    if (actPlayerRef.name != name) return false
    executeStep()
    if (isInHome()) nextValidToken()
    executeTokenKill()
    if (canRollAgain()) rollDice()
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

fun Game.getWinner(): String =
    players.find { it.standing == 1 }!!.name
