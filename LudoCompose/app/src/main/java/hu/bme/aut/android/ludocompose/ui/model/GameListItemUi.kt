package hu.bme.aut.android.ludocompose.ui.model

import hu.bme.aut.android.ludocompose.domain.model.GameListItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.time.LocalDateTime

data class GameListItemUi internal constructor(
    val id: Long = 0,
    val name: String = "",
    val date: String = LocalDate(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth
    ).toString(),
    val playerNames: List<String> = emptyList(),
)

fun GameListItem.toUiModel() = GameListItemUi(
    id = id,
    name = name,
    date = date.toString(),
    playerNames = playerNames,
)
