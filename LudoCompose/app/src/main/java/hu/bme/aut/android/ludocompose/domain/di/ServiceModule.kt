package hu.bme.aut.android.ludocompose.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.domain.services.GameServiceLocal
import hu.bme.aut.android.ludocompose.domain.services.ScoreService
import hu.bme.aut.android.ludocompose.domain.services.ScoreServiceLocal
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    @Singleton
    abstract fun bindGameService(
        gameServiceLocal: GameServiceLocal
    ): GameService

    @Binds
    @Singleton
    abstract fun bindScoreService(
        scoreServiceLocal: ScoreServiceLocal
    ): ScoreService
}
