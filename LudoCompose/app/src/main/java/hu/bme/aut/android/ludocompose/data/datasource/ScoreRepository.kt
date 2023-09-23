package hu.bme.aut.android.ludocompose.data.datasource

import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity

interface ScoreRepository {
    suspend fun getAll(): List<ScoreEntity>

    suspend fun get(name: String): ScoreEntity?

    suspend fun update(item: ScoreEntity)

    suspend fun insert(item: ScoreEntity)

    suspend fun delete(id: Long)
}