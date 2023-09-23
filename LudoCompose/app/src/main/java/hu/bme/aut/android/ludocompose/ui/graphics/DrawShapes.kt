package hu.bme.aut.android.ludocompose.ui.graphics

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import hu.bme.aut.android.ludocompose.ui.model.BoardUi
import hu.bme.aut.android.ludocompose.ui.model.ColorSequence
import hu.bme.aut.android.ludocompose.ui.model.DiceUi
import hu.bme.aut.android.ludocompose.ui.model.FieldUi
import hu.bme.aut.android.ludocompose.ui.model.GameUi
import hu.bme.aut.android.ludocompose.ui.model.border
import hu.bme.aut.android.ludocompose.ui.model.offset
import hu.bme.aut.android.ludocompose.ui.model.pointerRadius
import hu.bme.aut.android.ludocompose.ui.model.radius
import hu.bme.aut.android.ludocompose.ui.model.tokenRadius

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
    val fieldRadius = field.radius * scale
    val fieldBorder = field.border * scale

    drawRing(offset, fieldRadius, fieldBorder, field.color)

    val tokenColor = field.tokenColor
    if (tokenColor != null) {
        val tokenRadius = field.tokenRadius * scale
        val pointerRadius = field.pointerRadius * scale

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
    val offset = dice.offset * scale
    val radius = dice.radius * scale
    val border = dice.border * scale

    drawRing(offset, radius, border, dice.color)
}

fun DrawScope.drawGame(game: GameUi, scale: Float = 1f) {
    drawBoard(game.board!!, scale)
    drawDice(game.dice!!, scale)
}
