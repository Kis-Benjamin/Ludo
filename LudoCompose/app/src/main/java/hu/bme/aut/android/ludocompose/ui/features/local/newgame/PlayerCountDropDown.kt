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

package hu.bme.aut.android.ludocompose.ui.features.local.newgame

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme

@ExperimentalMaterial3Api
@Composable
fun PlayerCountDropDown(
    onPlayerCountSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = typography.labelLarge,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f, label = ""
    )

    val playerCounts = stringArrayResource(R.array.new_game_player_count)

    var selectedPlayerCount by remember { mutableIntStateOf(2) }

    val shape = RoundedCornerShape(5.dp)

    Surface(
        modifier = modifier
            .clip(shape = shape)
            .width(TextFieldDefaults.MinWidth)
            .background(colorScheme.background)
            .height(TextFieldDefaults.MinHeight)
            .clickable(enabled = enabled) { expanded = true },
        shape = shape
    ) {
        Row(
            modifier = modifier
                .width(TextFieldDefaults.MinWidth)
                .height(TextFieldDefaults.MinHeight)
                .clip(shape = shape),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                modifier = Modifier
                    .weight(weight = 8f),
                text = playerCounts[selectedPlayerCount - 1],
                style = style
            )
            IconButton(
                modifier = Modifier
                    .rotate(degrees = angle)
                    .weight(weight = 1.5f),
                onClick = { expanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            DropdownMenu(
                modifier = modifier
                    .width(TextFieldDefaults.MinWidth)
                    .clip(shape = shape),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                playerCounts.forEachIndexed { index, playerCount ->
                    if (index >= 1) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = playerCount,
                                    style = style
                                )
                            },
                            onClick = {
                                expanded = false
                                selectedPlayerCount = index + 1
                                onPlayerCountSelected(selectedPlayerCount)
                            },
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview
fun PlayerCountDropDown_Preview() {
    LudoComposeTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlayerCountDropDown(
                onPlayerCountSelected = {
                    //printk(it)
                }
            )
        }
    }
}
