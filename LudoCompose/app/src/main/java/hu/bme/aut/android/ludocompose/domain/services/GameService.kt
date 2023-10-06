package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.domain.model.Game
import hu.bme.aut.android.ludocompose.domain.model.GameListItem

interface GameService {
    val hasActive: Boolean

    suspend fun getActive(): Game

    suspend fun start(playerNames: List<String>)

    suspend fun getList(): List<GameListItem>

    suspend fun load(id: Long)

    suspend fun save(name: String)

    suspend fun delete(id: Long)

    suspend fun select()

    suspend fun step(): Boolean
}