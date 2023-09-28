package hu.bme.aut.android.ludocompose.features.savegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.common.NormalTextField
import hu.bme.aut.android.ludocompose.ui.util.UiEvent
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun SaveGameScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    saveGameViewModel: SaveGameViewModel = hiltViewModel()
) {
    val state by saveGameViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        saveGameViewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.Success -> {
                    onSuccess()
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(uiEvent.message.asString(context))
                    }
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp)),
    ) {
        Column(
            modifier = Modifier
                .background(color = colorScheme.tertiaryContainer)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NormalTextField(
                value = state.name,
                label = stringResource(id = R.string.save_game_enter_game_name),
                onValueChange = { saveGameViewModel.setName(it) },
                onDone = { keyboardController?.hide() },
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = { saveGameViewModel.save() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
            ) {
                Text(text = stringResource(id = R.string.save_game_button))
            }
        }
    }
}