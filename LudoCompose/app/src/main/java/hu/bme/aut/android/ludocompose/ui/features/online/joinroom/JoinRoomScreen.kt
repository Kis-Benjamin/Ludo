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

package hu.bme.aut.android.ludocompose.ui.features.online.joinroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.features.common.load.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.features.common.uievent.UiEventHandler

@Composable
fun JoinRoomScreen(
    snackbarHostState: SnackbarHostState,
    onSuccess: () -> Unit,
    joinRoomViewModel: JoinRoomViewModel = hiltViewModel()
) {
    val state by joinRoomViewModel.state.collectAsStateWithLifecycle()

    UiEventHandler(joinRoomViewModel.uiEvent, snackbarHostState, onSuccess)

    LoadingScreen(joinRoomViewModel.loadingViewModel) {
        if (state.rooms.isEmpty()) {
            Text(text = stringResource(id = R.string.join_room_list_empty))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val shape = RoundedCornerShape(15.dp)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(15.dp)
                        .clip(shape)
                        .background(colorScheme.secondaryContainer, shape)
                ) {
                    itemsIndexed(state.rooms, key = { _, room -> room.id }) { index, room ->
                        val isSelected = index == state.selectedIndex

                        ListItem(
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(shape)
                                .clickable {
                                    joinRoomViewModel.select(index)
                                },
                            headlineContent = {
                                Text(text = room.name)
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = if (isSelected) {
                                    colorScheme.onTertiary
                                } else {
                                    colorScheme.surface
                                }
                            ),
                        )
                    }
                }
                Button(
                    onClick = { joinRoomViewModel.join() },
                    enabled = state.selectedIndex != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp, bottom = 30.dp)
                ) {
                    Text(text = stringResource(id = R.string.join_room_button))
                }
            }
        }
    }
}