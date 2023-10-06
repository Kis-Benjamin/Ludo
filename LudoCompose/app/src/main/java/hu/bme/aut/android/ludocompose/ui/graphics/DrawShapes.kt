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

package hu.bme.aut.android.ludocompose.ui.graphics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import hu.bme.aut.android.ludocompose.ui.model.Constants
import hu.bme.aut.android.ludocompose.ui.model.BoardUi
import hu.bme.aut.android.ludocompose.ui.model.ColorSequence
import hu.bme.aut.android.ludocompose.ui.model.DiceUi
import hu.bme.aut.android.ludocompose.ui.model.FieldUi
import hu.bme.aut.android.ludocompose.ui.model.GameUi

fun DrawScope.drawDisk(offset: Offset, radius: Float, color: ColorSequence) {
    drawCircle(
        color = color.value,
        radius = radius,
        center = offset,
        style = Fill
    )
}

fun DrawScope.drawRing(offset: Offset, radius: Float, width: Float, color: ColorSequence) {
    drawDisk(offset, radius + width, color)
    drawDisk(offset, radius - width, ColorSequence.GRAY)
}


fun DrawScope.drawField(field: FieldUi, scale: Float) {
    val offset = field.offset * scale
    val fieldRadius = Constants.fieldRadius * scale
    val fieldBorder = Constants.fieldBorder * scale

    drawRing(offset, fieldRadius, fieldBorder, field.color)

    val tokenColor = field.tokenColor
    if (tokenColor != null) {
        val tokenRadius = Constants.tokenRadius * scale
        val pointerRadius = Constants.pointerRadius * scale

        drawDisk(offset, tokenRadius, tokenColor)
        if (field.isPointer)
            drawDisk(offset, pointerRadius, ColorSequence.BLACK)
    }
}

fun DrawScope.drawYardPoints(yardFields: List<List<FieldUi>>, scale: Float) {
    for (color in yardFields.indices) {
        for (pos in yardFields[color].indices) {
            drawField(yardFields[color][pos], scale)
        }
    }
}

fun DrawScope.drawTrackPoints(trackFields: List<FieldUi>, scale: Float) {
    for (pos in trackFields.indices) {
        drawField(trackFields[pos], scale)
    }
}

fun DrawScope.drawHomePoints(homeFields: List<List<FieldUi>>, scale: Float) {
    for (color in homeFields.indices) {
        for (pos in homeFields[color].indices) {
            drawField(homeFields[color][pos], scale)
        }
    }
}

fun DrawScope.drawBoard(board: BoardUi, scale: Float) {
    drawYardPoints(board.yardFields, scale)
    drawTrackPoints(board.trackFields, scale)
    drawHomePoints(board.homeFields, scale)
}

fun DrawScope.drawDice(dice: DiceUi, scale: Float) {
    val offset = Constants.diceOffset * scale
    val radius = Constants.diceRadius * scale
    val border = Constants.diceBorder * scale

    drawRing(offset, radius, border, dice.color)
}

fun DrawScope.drawGame(game: GameUi, scale: Float = 1f) {
    drawBoard(game.boardUi, scale)
    drawDice(game.diceUi, scale)
}
