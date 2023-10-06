package hu.bme.aut.android.ludocompose.data.datasource

import hu.bme.aut.android.ludocompose.data.dao.GameDao
import hu.bme.aut.android.ludocompose.data.pojos.GameWithPlayers
import hu.bme.aut.android.ludocompose.data.pojos.PlayerWithTokens
import javax.inject.Inject

class GameRepositoryDatabase @Inject constructor(
    private val gameDao: GameDao
) : GameRepository {
    override suspend fun getAll() = gameDao.getAll()

    override suspend fun get(name: String) = gameDao.get(name)

    override suspend fun get(id: Long) = gameDao.get(id)

    private suspend fun insert(playerWithTokens: PlayerWithTokens) {
        val id = gameDao.insert(playerWithTokens.player)
        playerWithTokens.tokens.forEach {
            it.playerId = id
            gameDao.insert(it)
        }
    }

    override suspend fun insert(gameWithPlayers: GameWithPlayers) {
        val id = gameDao.insert(gameWithPlayers.game)
        gameWithPlayers.players.forEach {
            it.player.gameId = id
            insert(it)
        }
    }

    override suspend fun delete(id: Long) {
        gameDao.delete(id)
    }
}