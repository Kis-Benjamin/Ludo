package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.domain.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.Player
import hu.bme.aut.android.ludocompose.domain.model.Token
import hu.bme.aut.android.ludocompose.domain.services.GameService
import javax.inject.Inject

class StartGameUseCase @Inject constructor(
    private val gameService: GameService,
) {
    suspend operator fun invoke(playerCount: Int, playerNames: List<String>) {
        val game = Game(
            playerCount = playerCount,
            players = playerNames.map { name ->
                Player(
                    name = name,
                    tokens = List(tokenCount) { Token() },
                )
            },
        )
        game.rollDice()
        gameService.game = game
    }
}