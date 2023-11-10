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

package hu.bme.aut.android.ludocompose.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hu.bme.aut.android.ludocompose.data.converters.LocalDateConverter
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "games",
    indices = [Index(value = ["id"], unique = true)]
)
@TypeConverters(LocalDateConverter::class)
data class GameEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "dice") var dice: Int,
    @ColumnInfo(name = "act_player") var actPlayer: Int,
) {
    constructor(name: String) : this(
        name = name,
        date = LocalDateTime.now().run {
            LocalDate(year, monthValue, dayOfMonth)
        },
        dice = (1..5).random(),
        actPlayer = 0,
    )
}

fun GameEntity.duplicate(name: String) = copy(id = null, name = name)
