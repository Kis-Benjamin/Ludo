package hu.bme.aut.android.ludocompose.domain.model

data class Field(
    var playerIndex: Int? = null,
    var isPointer: Boolean = false,
)

fun Field.set(playerIndex: Int?, isPointer: Boolean) {
    this.playerIndex = playerIndex
    this.isPointer = isPointer
}
