package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.ScoreItem

data class ScoreItemUi internal constructor(
    val id: Long = 0,
    val name: String = "",
    val winCount: String = "",
)

fun ScoreItem.toUiModel() = ScoreItemUi(
    id = id,
    name = name,
    winCount = winCount.toString(),
)
