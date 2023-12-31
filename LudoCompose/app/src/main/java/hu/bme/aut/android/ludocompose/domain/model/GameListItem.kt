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

package hu.bme.aut.android.ludocompose.domain.model

import hu.bme.aut.android.ludocompose.data.model.GameEntity
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

data class GameListItem internal constructor(
    val id: Long = 0,
    val name: String = "",
    val date: LocalDate = LocalDateTime.now().run {
        LocalDate(year, monthValue, dayOfMonth)
    },
    val playerNames: List<String> = emptyList(),
)

fun GameEntity.toDomainListModel(): GameListItem {
    require(id != null) { "Game id must not be null" }
    return GameListItem(
        id = id,
        name = name,
        date = date,
    )
}
