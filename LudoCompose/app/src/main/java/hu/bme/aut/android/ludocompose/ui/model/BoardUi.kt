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

package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.ui.model.Constants.trackMultiplier
import hu.bme.aut.android.ludocompose.session.model.BoardDTO

data class BoardUi internal constructor(
    val dice: DiceUi = DiceUi(),

    val yardFields: List<List<FieldUi>> = emptyList(),

    val trackFields: List<FieldUi> = emptyList(),

    val homeFields: List<List<FieldUi>> = emptyList(),
)

fun BoardDTO.toUiModel() = BoardUi(
    dice.toUiModel(),

    yardFields.mapIndexed { playerIndex, fieldDTOs ->
        fieldDTOs.mapIndexed { pieceIndex, fieldDTO ->
            fieldDTO.toUiModel(
                Constants.yardPoints[playerIndex][pieceIndex],
                ColorSequence.entries[playerIndex],
            )
        }
    },

    trackFields.mapIndexed { index, fieldDTO ->
        fieldDTO.toUiModel(
            Constants.trackPoints[index],
            if (index.mod(trackMultiplier) == 0)
                ColorSequence.entries[index / trackMultiplier]
            else ColorSequence.WHITE,
        )
    },

    homeFields.mapIndexed { playerIndex, fieldDTOs ->
        fieldDTOs.mapIndexed { pieceIndex, fieldDTO ->
            fieldDTO.toUiModel(
                Constants.homePoints[playerIndex][pieceIndex],
                ColorSequence.entries[playerIndex],
            )
        }
    },
)
