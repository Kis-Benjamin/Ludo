package hu.bme.aut.android.ludocompose.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.data.datasource.GameRepository
import hu.bme.aut.android.ludocompose.domain.services.GameService
import hu.bme.aut.android.ludocompose.domain.usecases.CheckActiveGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.DeleteGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.GetGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.LoadGamesUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.SaveGameUseCase
import hu.bme.aut.android.ludocompose.domain.usecases.StartGameUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameUseCaseModule {
    @Provides
    @Singleton
    fun provideLoadGamesUseCase(
        gameRepository: GameRepository,
    ) = LoadGamesUseCase(gameRepository)

    @Provides
    @Singleton
    fun provideLoadGameUseCase(
        gameRepository: GameRepository,
        gameService: GameService,
    ) = LoadGameUseCase(gameRepository, gameService)

    @Provides
    @Singleton
    fun provideSaveGameUseCase(
        gameRepository: GameRepository,
        gameService: GameService,
    ) = SaveGameUseCase(gameRepository, gameService)

    @Provides
    @Singleton
    fun provideDeleteGameUseCase(
        gameRepository: GameRepository,
    ) = DeleteGameUseCase(gameRepository)

    @Provides
    @Singleton
    fun provideStartGameUseCase(
        gameService: GameService,
    ) = StartGameUseCase(gameService)

    @Provides
    @Singleton
    fun provideCheckActiveGameUseCase(
        gameService: GameService,
    ) = CheckActiveGameUseCase(gameService)

    @Provides
    @Singleton
    fun provideGetGameUseCase(
        gameService: GameService,
    ) = GetGameUseCase(gameService)
}