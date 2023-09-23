package hu.bme.aut.android.ludocompose.domain.usecases

import hu.bme.aut.android.ludocompose.domain.services.GameService
import java.lang.IllegalStateException
import javax.inject.Inject

class GetGameUseCase @Inject constructor(
    private val gameService: GameService,
) {
    suspend operator fun invoke() = try {
        Result.success(checkNotNull(gameService.game) {"No game loaded!" })
    } catch (e: Exception) {
        Result.failure(e)
    }
}
