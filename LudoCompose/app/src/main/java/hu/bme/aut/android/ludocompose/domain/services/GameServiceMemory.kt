package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.domain.model.Game
import javax.inject.Inject

class GameServiceMemory @Inject constructor() : GameService {
    override var game: Game? = null
}