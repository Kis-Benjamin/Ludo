package hu.bme.aut.android.ludocompose.domain.usecases

import android.util.Log
import hu.bme.aut.android.ludocompose.domain.services.GameService
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetGameUseCase @Inject constructor(
    private val gameService: GameService,
) {
    suspend operator fun invoke() = try {
        var i = 0
        while (++i <= 10 && gameService.game == null) {
            Log.d("GetGameUseCase", "Waiting for game to load...\nAttempt: $i")
            delay(1000)
        }
        Result.success(checkNotNull(gameService.game) { "No game loaded!" })
    } catch (e: Exception) {
        Result.failure(e)
    }
}
