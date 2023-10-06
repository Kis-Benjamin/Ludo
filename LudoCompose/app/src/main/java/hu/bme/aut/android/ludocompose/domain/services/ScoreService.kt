package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.domain.model.ScoreItem

interface ScoreService {
    suspend fun getAll(): List<ScoreItem>

    suspend fun save(name: String)

    suspend fun delete(id: Long)
}