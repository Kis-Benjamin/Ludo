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

package hu.bme.aut.android.ludocompose.ui.features.main.menu

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventHandler

@ExperimentalMaterial3Api
@Composable
fun MenuScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToLocal: () -> Unit,
    onNavigateToOnline: () -> Unit,
    onNavigateToAbout: () -> Unit,
    menuViewModel: MenuViewModel = hiltViewModel()
) {
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = menuViewModel.authContract,
        onResult = menuViewModel::onResult,
    )

    LaunchedEffect(true) {
        menuViewModel.initialize()
    }

    UiEventHandler(menuViewModel.uiEvent, snackbarHostState, onNavigateToOnline)

    val style = typography.labelLarge
    val modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier,
            onClick = onNavigateToLocal,
        ) {
            Text(
                text = stringResource(id = R.string.menu_local),
                style = style
            )
        }
        Button(
            modifier = modifier,
            enabled = state.isOnlineEnabled,
            onClick = { menuViewModel.launch(launcher::launch) },
        ) {
            Text(
                text = stringResource(id = R.string.menu_online),
                style = style
            )
        }
        Button(
            modifier = modifier,
            onClick = onNavigateToAbout,
        ) {
            Text(
                text = stringResource(id = R.string.menu_about),
                style = style
            )
        }
    }
}
