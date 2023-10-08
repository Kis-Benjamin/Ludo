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

package hu.bme.aut.android.ludocompose.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.bme.aut.android.ludocompose.data.converters.LocalDateConverter
import hu.bme.aut.android.ludocompose.data.converters.TokenStateConverter
import hu.bme.aut.android.ludocompose.data.dao.GameDao
import hu.bme.aut.android.ludocompose.data.dao.ScoreDao
import hu.bme.aut.android.ludocompose.data.model.GameEntity
import hu.bme.aut.android.ludocompose.data.model.PlayerEntity
import hu.bme.aut.android.ludocompose.data.model.ScoreEntity
import hu.bme.aut.android.ludocompose.data.model.TokenEntity

@Database(
    entities = [
        GameEntity::class,
        PlayerEntity::class,
        TokenEntity::class,
        ScoreEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateConverter::class, TokenStateConverter::class)
abstract class LudoDatabase : RoomDatabase() {
    abstract val gameDao: GameDao
    abstract val scoreDao: ScoreDao
}