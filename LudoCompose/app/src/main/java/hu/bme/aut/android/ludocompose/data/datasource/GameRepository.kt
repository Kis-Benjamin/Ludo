package hu.bme.aut.android.ludocompose.data.datasource

import hu.bme.aut.android.ludocompose.data.entities.GameEntity
import hu.bme.aut.android.ludocompose.data.pojos.GameWithPlayers

interface GameRepository {
    suspend fun getAll(): List<GameEntity>

    suspend fun get(id: Long): GameWithPlayers

    suspend fun insert(gameWithPlayers: GameWithPlayers)

    suspend fun delete(gameId: Long)
}