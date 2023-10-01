package hu.bme.aut.android.ludocompose.domain.model

data class Board(
    val yardFields: List<List<Field>> = List(4) { List(4) { Field() } },
    val trackFields: List<Field> = List(40) { Field() },
    val homeFields: List<List<Field>> = List(4) { List(4) { Field() } }
)
