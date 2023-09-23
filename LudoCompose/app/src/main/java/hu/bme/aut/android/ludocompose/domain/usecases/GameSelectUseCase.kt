package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.domain.model.select
import hu.bme.aut.android.ludocompose.domain.services.GameService
import javax.inject.Inject

class GameSelectUseCase @Inject constructor(
    private val gameService: GameService,
) {
    suspend operator fun invoke() = gameService.game?.select()
}