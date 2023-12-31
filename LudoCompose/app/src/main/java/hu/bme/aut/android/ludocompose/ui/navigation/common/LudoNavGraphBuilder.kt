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

package hu.bme.aut.android.ludocompose.ui.navigation.common

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navigation
import hu.bme.aut.android.ludocompose.ui.common.enterTransition
import hu.bme.aut.android.ludocompose.ui.common.exitTransition

class LudoNavGraphBuilder(
    private val navGraphBuilder: NavGraphBuilder,
    private val onNavigate: (Screen) -> Unit,
) {
    fun composable(
        screen: Screen,
        argument: NamedNavArgument? = null,
        content: @Composable (NavBackStackEntry) -> Unit
    ) {
        navGraphBuilder.composable(
            route = screen.route,
            arguments = argument?.let { listOf(it) } ?: emptyList(),
            enterTransition = enterTransition,
            exitTransition = exitTransition,
        ) {
            onNavigate(screen)
            content(it)
        }
    }

    fun dialog(
        screen: Screen,
        content: @Composable (NavBackStackEntry) -> Unit
    ) {
        navGraphBuilder.dialog(screen.route) {
            content(it)
        }
    }
    fun navigation(
        startDestination: Screen,
        route: Screen,
        content: LudoNavGraphBuilder.() -> Unit
    ) {
        navGraphBuilder.navigation(
            startDestination = startDestination.route,
            route = route.route,
        ) {
            LudoNavGraphBuilder(this, onNavigate).apply(content)
        }
    }

    fun NavController.navigate(destination: Screen, popUpTo: Screen) {
        navigate(destination.route) {
            popUpTo(popUpTo.route)
        }
    }
}
