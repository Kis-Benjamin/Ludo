package hu.bme.aut.android.ludocompose.ui.util

import hu.bme.aut.android.ludocompose.ui.model.UiText

sealed class UiEvent {
    object Success: UiEvent()
    data class Failure(val message: UiText): UiEvent()
}