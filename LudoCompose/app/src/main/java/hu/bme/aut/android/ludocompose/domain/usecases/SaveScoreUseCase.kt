package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity
import hu.bme.aut.android.ludocompose.domain.model.getWinner
import hu.bme.aut.android.ludocompose.domain.services.GameService
import javax.inject.Inject

class SaveScoreUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository,
    private val gameService: GameService,
) {
    suspend operator fun invoke() = try {
        val game = checkNotNull(gameService.game) { "No game loaded!" }
        val name = game.getWinner()
        val scoreEntity = scoreRepository.get(name)
        if (scoreEntity != null) {
            val newScoreEntity = scoreEntity.copy(winCount = scoreEntity.winCount + 1)
            scoreRepository.update(newScoreEntity)
        } else {
            scoreRepository.insert(ScoreEntity(name = name, winCount = 1))
        }
        gameService.game = null
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}