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

package hu.bme.aut.android.ludocompose.ui.features.local.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R

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
    val modifier = Modifier.fillMaxWidth().padding(20.dp)

    menuViewModel.load()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier,
            onClick = onNavigateToNewGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_new_game),
                style = style
            )
        }
        Button(
            modifier = modifier,
            onClick = onNavigateToLoadGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_load_game),
                style = style
            )
        }
        Button(
            modifier = modifier,
            onClick = onNavigateToSaveGame,
            enabled = state.hasActiveGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_save_game),
                style = style
            )
        }
        Button(
            modifier = modifier,
            onClick = onNavigateToGame,
            enabled = state.hasActiveGame,
        ) {
            Text(
                text = stringResource(id = R.string.menu_resume_game),
                style = style
            )
        }
        Button(
            modifier = modifier,
            onClick = onNavigateToScoreboard
        ) {
            Text(
                text = stringResource(id = R.string.menu_score_board),
                style = style
            )
        }
    }
}
