package hu.bme.aut.android.ludocompose.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.data.datasource.GameRepositoryDatabase
import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepositoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepositoryDatabase: GameRepositoryDatabase
    ): GameRepository

    @Binds
    @Singleton
    abstract fun bindScoreRepository(
        scoreRepositoryDatabase: ScoreRepositoryDatabase
    ): ScoreRepository
}
