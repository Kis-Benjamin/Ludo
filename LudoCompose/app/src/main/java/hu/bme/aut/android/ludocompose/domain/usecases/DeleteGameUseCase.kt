package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import javax.inject.Inject

class DeleteGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(gameId: Long) {
        gameRepository.delete(gameId)
    }
}