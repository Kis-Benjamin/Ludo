package hu.bme.aut.android.ludocompose.features.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.common.LudoAppBar

@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    onNavigateToNewGame: () -> Unit,
    onNavigateToLoadGame: () -> Unit,
    onNavigateToSaveGame: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToScoreboard: () -> Unit,
    menuViewModel: MenuViewModel = hiltViewModel()
) {
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    val style = typography.labelLarge

    menuViewModel.load()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToNewGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_new_game),
                style = style
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToLoadGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_load_game),
                style = style
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToSaveGame,
            enabled = state.hasActiveGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_save_game),
                style = style
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToGame,
            enabled = state.hasActiveGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_resume_game),
                style = style
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToScoreboard
        ) {
            Text(
                text = stringResource(id = R.string.menu_score_board),
                style = style
            )
        }
    }
}
