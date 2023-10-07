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

package hu.bme.aut.android.ludocompose.navigation.common

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost


@Composable
fun LudoNavHost(
    navController: NavHostController,
    screen: IScreen,
    onTitleChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        { EnterTransition.None },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        { ExitTransition.None },
    builder: LudoNavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = screen.route,
        modifier = modifier,
        contentAlignment = contentAlignment,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
    ) {
        LudoNavGraphBuilder(this, onTitleChange).apply(builder)
    }
}
