/*
 * Copyright © 2023 Benjamin Kis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
