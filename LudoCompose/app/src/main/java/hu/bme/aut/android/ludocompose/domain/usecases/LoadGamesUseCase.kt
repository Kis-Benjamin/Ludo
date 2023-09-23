package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import javax.inject.Inject

class LoadGamesUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke() = try {
        val gameEntities = gameRepository.getAll()
        val games = gameEntities.map { it.toDomainModel() }
        Result.success(games)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
