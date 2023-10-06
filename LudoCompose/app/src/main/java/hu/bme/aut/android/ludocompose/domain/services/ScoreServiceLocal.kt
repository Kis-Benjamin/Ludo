package hu.bme.aut.android.ludocompose.domain.services

import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.domain.converters.toDataModel
import hu.bme.aut.android.ludocompose.domain.converters.toDomainModel
import hu.bme.aut.android.ludocompose.domain.model.ScoreItem
import javax.inject.Inject

class ScoreServiceLocal @Inject constructor(
    private val scoreRepository: ScoreRepository,
) : ScoreService {
    override suspend fun getAll(): List<ScoreItem> {
        val scoreEntities = scoreRepository.getAll()
        return scoreEntities.map { it.toDomainModel() }
    }

    override suspend fun save(name: String) {
        val scoreEntity = scoreRepository.get(name)?.let {
            it.copy(winCount = it.winCount + 1)
        } ?: ScoreItem(name = name, winCount = 1).toDataModel()
        scoreRepository.insert(scoreEntity)
    }

    override suspend fun delete(id: Long) {
        scoreRepository.delete(id)
    }
}