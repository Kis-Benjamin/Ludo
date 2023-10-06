/*
 * Copyright © 2023 Benjamin Kis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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