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

package hu.bme.aut.android.ludocompose.ui.features.online.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventHandler

@Composable
fun RoomScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    onClose: () -> Unit,
    isHost: Boolean,
    roomViewModel: RoomViewModel = hiltViewModel()
) {
    val state by roomViewModel.state.collectAsStateWithLifecycle()

    val shape = RoundedCornerShape(15.dp)

    UiEventHandler(roomViewModel.uiEvent, snackbarHostState, onSuccess, onClose)

    LoadingScreen(roomViewModel.loadingViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .clip(shape)
                .background(MaterialTheme.colorScheme.secondaryContainer, shape),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.02f))
            Text(
                text = state.room.name,
                textAlign = TextAlign.Center,
                style = typography.headlineLarge,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.room_host_name),
                    style = typography.headlineMedium,
                )
                Text(
                    text = state.room.host.name,
                    style = typography.headlineMedium,
                )
            }
            if (state.room.users.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.room_user_list_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(15.dp)
                        .clip(shape)
                ) {
                    items(state.room.users) { user ->
                        ListItem(
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(shape)
                                .background(MaterialTheme.colorScheme.onSecondaryContainer, shape),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = null,
                                    tint = user.color,
                                    modifier = Modifier.size(40.dp),
                                )
                            },
                            headlineContent = {
                                Text(text = user.name)
                            },
                        )
                    }
                }
                if (isHost) {
                    Button(
                        onClick = { roomViewModel.start() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
                    ) {
                        Text(text = stringResource(id = R.string.room_start_game_button))
                    }
                } else {
                    Button(
                        onClick = { roomViewModel.changeReadyState() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = if (state.ready) R.string.room_unready_button
                                else R.string.room_ready_button
                            )
                        )
                    }
                }
            }
            if (isHost) {
                Button(
                    onClick = { roomViewModel.close() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
                ) {
                    Text(text = stringResource(id = R.string.room_close_button))
                }
            } else {
                Button(
                    onClick = { roomViewModel.leave() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
                ) {
                    Text(
                        text = stringResource(R.string.room_leave_button)
                    )
                }
            }
        }
    }
}
