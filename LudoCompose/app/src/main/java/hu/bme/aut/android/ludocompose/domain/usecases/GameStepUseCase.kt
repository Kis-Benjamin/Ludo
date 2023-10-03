package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.domain.services.GameService
import javax.inject.Inject

class GameStepUseCase @Inject constructor(
    private val gameService: GameService,
) {
    suspend operator fun invoke() = gameService.game?.step()
}