package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.Game
import kotlin.random.Random

data class GameUi(
    val boardUi: BoardUi = BoardUi,
    val diceUi: DiceUi = DiceUi(),
    val seed: Int = Random.nextInt(),
)

fun Game.toUiModel() = GameUi(
    boardUi = BoardUi.apply { update(board) },
    diceUi = DiceUi(dice, ColorSequence.entries[actPlayerIndex]),
)
