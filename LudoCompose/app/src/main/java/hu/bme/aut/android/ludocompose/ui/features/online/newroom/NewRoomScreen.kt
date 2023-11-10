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

package hu.bme.aut.android.ludocompose.ui.features.online.newroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.common.NormalTextField
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventHandler

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun NewRoomScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    newRoomViewModel: NewRoomViewModel = hiltViewModel()
) {
    val state by newRoomViewModel.state.collectAsStateWithLifecycle()

    UiEventHandler(newRoomViewModel.uiEvent, snackbarHostState, onSuccess)

    val shape = RoundedCornerShape(30.dp)
    val color = MaterialTheme.colorScheme.tertiaryContainer

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .clip(shape),
    ) {
        Column(
            modifier = Modifier
                .background(color)
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NormalTextField(
                value = state.name,
                label = stringResource(id = R.string.new_room_enter_game_name),
                onValueChange = { newRoomViewModel.setName(it) },
                onDone = { keyboardController?.hide() },
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = { newRoomViewModel.create() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
            ) {
                Text(text = stringResource(id = R.string.new_room_button))
            }
        }
    }
}