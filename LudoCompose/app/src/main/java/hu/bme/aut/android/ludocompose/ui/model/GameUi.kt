package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.Game

data class GameUi(
    val board: BoardUi? = null,
    val dice: DiceUi? = null,
)

fun Game.toUiModel() = GameUi(
    board = board.toUiModel(),
    dice = DiceUi(dice, ColorSequence.values()[actPlayerIndex]),
)
