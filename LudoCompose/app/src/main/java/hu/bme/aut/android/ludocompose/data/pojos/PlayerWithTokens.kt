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

package hu.bme.aut.android.ludocompose.data.pojos

import androidx.room.Embedded
import androidx.room.Relation
import hu.bme.aut.android.ludocompose.data.entities.PlayerEntity
import hu.bme.aut.android.ludocompose.data.entities.TokenEntity

data class PlayerWithTokens(
    @Embedded val player: PlayerEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "player_id",
        entity = TokenEntity::class,
    ) val tokens: List<TokenEntity>
)
