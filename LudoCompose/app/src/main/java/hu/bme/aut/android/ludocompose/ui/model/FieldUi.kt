package hu.bme.aut.android.ludocompose.ui.model

import androidx.compose.ui.geometry.Offset
import hu.bme.aut.android.ludocompose.domain.model.Field

data class FieldUi(
    val offset: Offset,
    val color: ColorSequence = ColorSequence.GRAY,
    var tokenColor: ColorSequence? = null,
    var isPointer: Boolean = false,
)

fun FieldUi.update(field: Field) {
    tokenColor = field.playerIndex?.let { ColorSequence.values()[it] }
    isPointer = field.isPointer
}
