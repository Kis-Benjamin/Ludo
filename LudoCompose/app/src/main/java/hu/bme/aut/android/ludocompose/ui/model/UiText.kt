package hu.bme.aut.android.ludocompose.ui.model

import android.content.Context
import androidx.annotation.StringRes
import hu.bme.aut.android.ludocompose.R

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class StringResource(@StringRes val id: Int, vararg val formatArgs: Any): UiText()

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> this.value
            is StringResource -> context.getString(this.id, *this.formatArgs)
        }
    }
}

fun Throwable.toUiText(): UiText {
    val message = this.message.orEmpty()
    return if (message.isBlank()) {
        UiText.StringResource(R.string.unknown_error_message)
    } else {
        UiText.DynamicString(message)
    }
}

fun String.toUiText(): UiText {
    return UiText.DynamicString(this)
}