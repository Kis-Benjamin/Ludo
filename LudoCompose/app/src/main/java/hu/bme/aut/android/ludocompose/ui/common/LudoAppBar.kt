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

package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme
import hu.bme.aut.android.ludocompose.ui.animation.transitionSpec
import hu.bme.aut.android.ludocompose.ui.animation.startEnterTransition
import hu.bme.aut.android.ludocompose.ui.animation.startExitTransition

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun LudoAppBar(
    modifier: Modifier = Modifier,
    title: String,
    enableNavigationBack: Boolean = true,
    onNavigationClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            AnimatedContent(
                targetState = title,
                transitionSpec = transitionSpec,
                label = "TitleChangeAnimation"
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            AnimatedVisibility(
                visible = enableNavigationBack,
                enter = startEnterTransition,
                exit = startExitTransition,
                label = "NavigationIconVisibilityAnimation"
            ) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Main container navigation icon"
                    )
                }
            }
        },
        actions = {},
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun LudoAppBar_Preview() {
    LudoComposeTheme {
        LudoAppBar(
            title = "Ludo",
        )
    }
}
