package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import javax.inject.Inject

class LoadScoresUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository,
) {
    suspend operator fun invoke() = try {
        val scoreEntities = scoreRepository.getAll()
        Result.success(scoreEntities
            .map { it.toDomainModel() }
            .sortedByDescending { it.winCount }
        )
    } catch (e: Exception) {
        Result.failure(e)
    }
}
