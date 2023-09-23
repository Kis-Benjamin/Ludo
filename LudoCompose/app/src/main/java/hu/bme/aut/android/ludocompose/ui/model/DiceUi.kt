package hu.bme.aut.android.ludocompose.ui.model

import androidx.compose.ui.geometry.Offset

data class DiceUi(
    var value: Int,
    var color: ColorSequence = ColorSequence.GRAY,
)

val DiceUi.radius: Float get() = BoardConstants.diceRadius
val DiceUi.offset: Offset get() = BoardConstants.diceOffset
val DiceUi.border: Float get() = BoardConstants.diceBorder
