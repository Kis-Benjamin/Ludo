package hu.bme.aut.android.ludocompose.features.newgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
fun NewGameScreen(
    onSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
    newGameViewModel: NewGameViewModel = hiltViewModel()
) {
    val state by newGameViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        newGameViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    onSuccess()
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }

    val playerNamesLabel = arrayOf(
        R.string.new_game_enter_player_one_name,
        R.string.new_game_enter_player_two_name,
        R.string.new_game_enter_player_three_name,
        R.string.new_game_enter_player_four_name,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(id = R.string.new_game_enter_player_count))
        PlayerCountDropDown(
            onPlayerCountSelected = { newGameViewModel.changePlayerCount(it) }
        )
        Text(text = stringResource(id = R.string.new_game_enter_player_names))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            for (i in 0 until 4) {
                NormalTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.playerNames[i],
                    onValueChange = { newGameViewModel.changePlayerName(i, it) },
                    label = stringResource(id = playerNamesLabel[i]),
                    enabled = i < state.playerCount,
                    onDone = { keyboardController?.hide() }
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { newGameViewModel.startGame() }
        ) {
            Text(
                text = stringResource(id = R.string.new_game_button),
                style = typography.labelLarge
            )
        }
    }
}
