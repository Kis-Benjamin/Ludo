package hu.bme.aut.android.ludocompose.data.datasource

import hu.bme.aut.android.ludocompose.data.dao.ScoreDao
import hu.bme.aut.android.ludocompose.data.entities.ScoreEntity
import javax.inject.Inject

class ScoreRepositoryDatabase @Inject constructor(
    private val scoreDao: ScoreDao
) : ScoreRepository {
    override suspend fun getAll() = scoreDao.getAll()

    override suspend fun get(name: String) = scoreDao.get(name)

    override suspend fun update(item: ScoreEntity) = scoreDao.update(item)

    override suspend fun insert(item: ScoreEntity) = scoreDao.insert(item)

    override suspend fun delete(id: Long) = scoreDao.delete(id)
}