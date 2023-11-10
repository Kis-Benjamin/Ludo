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

package hu.bme.aut.android.ludocompose.session.model

import hu.bme.aut.android.ludocompose.domain.model.Board

data class BoardDTO(
    val dice: DiceDTO = DiceDTO(),
    val selectEnabled: Boolean = false,
    val stepEnabled: Boolean = false,
    val yardFields: List<List<FieldDTO>> = emptyList(),
    val trackFields: List<FieldDTO> = emptyList(),
    val homeFields: List<List<FieldDTO>> = emptyList(),
)

fun Board.toSessionModel() = BoardDTO(
    dice = dice.toSessionModel(),
    selectEnabled = selectEnabled,
    stepEnabled = stepEnabled,
    yardFields = yardFields.map { it.map { it.toSessionModel() } },
    trackFields = trackFields.map { it.toSessionModel() },
    homeFields = homeFields.map { it.map { it.toSessionModel() } },
)
