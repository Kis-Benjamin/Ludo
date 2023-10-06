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

import hu.bme.aut.android.ludocompose.domain.model.Constants.playerCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.tokenCount
import hu.bme.aut.android.ludocompose.domain.model.Constants.trackSize

data object Board {
    val yardFields: List<List<Field>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                Field()
            }
        }
    }

    val trackFields: List<Field> by lazy {
        List(trackSize) { index ->
            Field()
        }
    }

    val homeFields: List<List<Field>> by lazy {
        List(playerCount) { playerIndex ->
            List(tokenCount) { tokenIndex ->
                Field()
            }
        }
    }

    fun reset() {
        yardFields.forEach { fields ->
            fields.forEach { field ->
                field.reset()
            }
        }
        trackFields.forEach { field ->
            field.reset()
        }
        homeFields.forEach { fields ->
            fields.forEach { field ->
                field.reset()
            }
        }
    }
}
