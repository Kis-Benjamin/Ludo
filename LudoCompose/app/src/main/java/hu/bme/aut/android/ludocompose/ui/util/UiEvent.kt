package hu.bme.aut.android.ludocompose.ui.util

import hu.bme.aut.android.ludocompose.ui.model.UiText

sealed class UiEvent {
    data object Success: UiEvent()
    data class Settled(val message: UiText): UiEvent()
    data class Failure(val message: UiText): UiEvent()
}