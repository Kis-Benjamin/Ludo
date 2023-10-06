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

package hu.bme.aut.android.ludocompose.features.local.scoreboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import hu.bme.aut.android.ludocompose.ui.common.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.common.UiEventHandler

@Composable
fun ScoreBoardScreen(
    snackbarHostState: SnackbarHostState,
    scoreBoardViewModel: ScoreBoardViewModel = hiltViewModel()
) {
    val loadingState by scoreBoardViewModel.loadingState.collectAsStateWithLifecycle()
    val state by scoreBoardViewModel.state.collectAsStateWithLifecycle()

    UiEventHandler(scoreBoardViewModel.uiEvent, snackbarHostState)

    LoadingScreen(loadingState) {
        if (state.scores.isEmpty()) {
            Text(text = stringResource(id = R.string.score_board_empty))
        } else {
            val shape = RoundedCornerShape(15.dp)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
                    .clip(shape)
                    .background(colorScheme.tertiaryContainer, shape)
            ) {
                itemsIndexed(state.scores, key = { _, score -> score.id }) { _, score ->
                    ListItem(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(shape)
                            .background(colorScheme.onTertiaryContainer, shape),
                        headlineContent = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = score.name)
                                Text(text = score.winCount)
                            }
                        },
                        trailingContent = {
                            IconButton(
                                onClick = {
                                    scoreBoardViewModel.delete(score.id)
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    )
                }
            }
        }
    }
}
