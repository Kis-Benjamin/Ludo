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

import androidx.compose.ui.geometry.Offset
import hu.bme.aut.android.ludocompose.session.model.FieldDTO

data class FieldUi internal constructor(
    val offset: Offset,
    val color: ColorSequence = ColorSequence.GRAY,
    val pieceColor: ColorSequence? = null,
    val pointer: Boolean = false,
)

fun FieldDTO.toUiModel(offset: Offset, color: ColorSequence) = FieldUi(
    offset = offset,
    color = color,
    pieceColor = this.pieceColor?.let { ColorSequence.entries[it] },
    pointer = pointer,
)
