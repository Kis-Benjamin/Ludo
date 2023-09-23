package hu.bme.aut.android.ludocompose.data.dao

import androidx.room.*
import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores")
    suspend fun getAll(): List<ScoreEntity>

    @Query("SELECT * FROM scores WHERE name = :name")
    suspend fun get(name: String): ScoreEntity?

    @Update
    suspend fun update(item: ScoreEntity)

    @Insert
    suspend fun insert(item: ScoreEntity)

    @Query("DELETE FROM scores WHERE id = :id")
    suspend fun delete(id: Long)
}
