package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDataModel
import hu.bme.aut.android.ludocompose.domain.services.GameService
import kotlinx.datetime.LocalDate
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import javax.inject.Inject

class SaveGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val gameService: GameService,
) {
    suspend operator fun invoke(name: String) = try {
        val gameEntities = gameRepository.getAll()
        gameEntities.firstOrNull { it.name == name }?.let {
            throw IllegalArgumentException("Game already exists")
        }
        val date = LocalDateTime.now()
        val localDate = LocalDate(
            date.year,
            date.monthValue,
            date.dayOfMonth,
        )
        val game = checkNotNull(gameService.game) { "No game loaded" }
        val gameEntity = game.toDataModel(name, localDate)
        gameRepository.insert(gameEntity)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}