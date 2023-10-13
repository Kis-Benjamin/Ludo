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

package hu.bme.aut.android.ludocompose.ui.features.main.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.features.common.LoadingScreen
import hu.bme.aut.android.ludocompose.ui.graphics.drawGame

@Composable
fun GameScreen(
    onGameEnded: () -> Unit,
    gameViewModel: GameViewModel,
) {
    val loadingState by gameViewModel.loadingState.collectAsStateWithLifecycle()
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LoadingScreen(loadingState) {
        Column {
            key(state) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .aspectRatio(1f)
                        .clip(RectangleShape)
                        .background(Gray)
                        .drawWithContent {
                            val average = (size.width + size.height) / 2
                            drawGame(state.game!!, average)
                            drawContent()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.game!!.diceUi.value,
                        modifier = Modifier.padding(bottom = 3.dp),
                        style = typography.displayLarge,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().weight(0.2f),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    onClick = { gameViewModel.select() },
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(16.dp),
                    enabled = state.isSelectEnabled
                ) {
                    Text(text = stringResource(id = R.string.game_select))
                }
                Button(
                    onClick = { gameViewModel.step(onGameEnded) },
                    modifier = Modifier
                        .weight(1f, true)
                        .padding(16.dp),
                ) {
                    Text(text = stringResource(id = R.string.game_step))
                }
            }
        }
    }
}
