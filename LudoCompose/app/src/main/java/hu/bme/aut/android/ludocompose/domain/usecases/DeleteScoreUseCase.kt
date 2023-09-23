package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import javax.inject.Inject

class DeleteScoreUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository,
) {
    suspend operator fun invoke(id: Long) = scoreRepository.delete(id)
}
