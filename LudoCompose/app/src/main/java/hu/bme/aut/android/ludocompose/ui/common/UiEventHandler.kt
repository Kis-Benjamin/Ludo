package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
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
            when (uiEvent) {
                is UiEvent.Success -> {
                    onSuccess(uiEvent)
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }
}
