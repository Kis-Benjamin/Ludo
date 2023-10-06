package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import hu.bme.aut.android.ludocompose.ui.model.UiText
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun UiEventHandler(
    uiEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    onSuccess: (UiEvent.Success) -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        uiEvent.collect { uiEvent ->
            fun send(message: UiText) {
                scope.launch {
                    snackbarHostState.showSnackbar(message.asString(context))
                }
            }
            when (uiEvent) {
                is UiEvent.Success -> {
                    onSuccess(uiEvent)
                }
                is UiEvent.Settled -> {
                    send(uiEvent.message)
                }
                is UiEvent.Failure -> {
                    send(uiEvent.message)
                }
            }
        }
    }
}
