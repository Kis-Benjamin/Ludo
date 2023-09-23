package hu.bme.aut.android.ludocompose.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.ludocompose.data.LudoDatabase
import hu.bme.aut.android.ludocompose.data.dao.GameDao
import hu.bme.aut.android.ludocompose.data.dao.ScoreDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabaseInstance(
        @ApplicationContext context: Context
    ): LudoDatabase = Room.databaseBuilder(
        context,
        LudoDatabase::class.java,
        "ludo.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideGameDao(
        ludoDatabase: LudoDatabase
    ): GameDao = ludoDatabase.gameDao

    @Provides
    @Singleton
    fun provideScoreDao(
        ludoDatabase: LudoDatabase
    ): ScoreDao = ludoDatabase.scoreDao
}
