package hu.bme.aut.android.ludocompose.data.dao

import androidx.room.*
import hu.bme.aut.android.ludocompose.data.entities.GameEntity
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity
import hu.bme.aut.android.ludocompose.data.entities.TokenEntity
import hu.bme.aut.android.ludocompose.data.pojos.GameWithPlayers

@Dao
interface GameDao {
    @Transaction
    @Query("SELECT * FROM games")
    suspend fun getAll(): List<GameEntity>

    @Transaction
    @Query("SELECT * FROM games WHERE name = :name")
    suspend fun get(name: String): GameEntity?

    @Transaction
    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun get(id: Long): GameWithPlayers

    @Insert
    suspend fun insert(game: GameEntity): Long

    @Insert
    suspend fun insert(player: PlayerEntity): Long

    @Insert
    suspend fun insert(token: TokenEntity): Long

    @Transaction
    @Query("DELETE FROM games WHERE id = :id")
    suspend fun delete(id: Long)
}
