package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.util.LoadingState
import hu.bme.aut.android.ludocompose.ui.model.toUiText

@Composable
fun LoadingScreen(
    loadingState: LoadingState,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (loadingState.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        } else if (loadingState.isError) {
            Text(
                text = loadingState.error?.toUiText()?.asString(context)
                    ?: stringResource(id = R.string.unknown_error_message)
            )
        } else {
            content()
        }
    }
}