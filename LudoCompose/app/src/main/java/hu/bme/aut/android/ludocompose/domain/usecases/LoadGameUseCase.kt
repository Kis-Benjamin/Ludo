package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import hu.bme.aut.android.ludocompose.domain.services.GameService
import javax.inject.Inject

class LoadGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameService: GameService,
) {
    suspend operator fun invoke(id: Long) {
        val gameEntity = gameRepository.get(id)
        val game = gameEntity.toDomainModel()
        gameService.game = game
    }
}