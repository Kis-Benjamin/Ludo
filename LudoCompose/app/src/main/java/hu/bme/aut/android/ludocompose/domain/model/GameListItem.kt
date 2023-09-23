package hu.bme.aut.android.ludocompose.domain.model

import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

data class GameListItem(
    val id: Long = 0,
    val name: String = "",
    val date: LocalDate = LocalDate(
        LocalDateTime.now().year,
        LocalDateTime.now().monthValue,
        LocalDateTime.now().dayOfMonth
    ),
    val playerNames: List<String> = emptyList(),
)
