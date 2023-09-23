package hu.bme.aut.android.ludocompose.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.data.datasource.ScoreRepository
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.domain.usecases.DeleteScoreUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadScoresUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.SaveScoreUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScoreUseCaseModule {
    @Provides
    @Singleton
    fun provideLoadScoresUseCase(
        scoreRepository: ScoreRepository,
    ) = LoadScoresUseCase(scoreRepository)

    @Provides
    @Singleton
    fun provideSaveScoreUseCase(
        scoreRepository: ScoreRepository,
        gameService: GameService,
    ) = SaveScoreUseCase(scoreRepository, gameService)

    @Provides
    @Singleton
    fun provideDeleteScoreUseCase(
        scoreRepository: ScoreRepository,
    ) = DeleteScoreUseCase(scoreRepository)
}