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

package hu.bme.aut.android.ludocompose.ui.navigation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import hu.bme.aut.android.ludocompose.ui.features.main.MenuScreen
import hu.bme.aut.android.ludocompose.ui.features.main.AboutScreen
import hu.bme.aut.android.ludocompose.ui.navigation.local.NavGraph as LocalNavGraph
import hu.bme.aut.android.ludocompose.ui.navigation.local.MenuScreen as Local_MenuScreen
import hu.bme.aut.android.ludocompose.ui.navigation.online.NavGraph as OnlineNavGraph
import hu.bme.aut.android.ludocompose.ui.navigation.online.MenuScreen as Online_MenuScreen
import hu.bme.aut.android.ludocompose.ui.navigation.common.LudoNavHost
import hu.bme.aut.android.ludocompose.ui.navigation.common.Screen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onNavigate: (Screen) -> Unit,
    navController: NavHostController,
) {
    LudoNavHost(
        navController = navController,
        startScreen = MenuScreen,
        onNavigate = onNavigate,
        modifier = modifier,
    ) {
        fun navigate(destination: Screen) {
            navController.navigate(destination, MenuScreen)
        }

        composable(MenuScreen) {
            MenuScreen(
                onNavigateToLocal = { navigate(LocalMenuScreen) },
                onNavigateToOnline = { navigate(OnlineMenuScreen) },
                onNavigateToAbout = { navigate(AboutScreen) },
            )
        }
        dialog(AboutScreen) {
            AboutScreen()
        }
        navigation(
            startDestination = Local_MenuScreen,
            route = LocalMenuScreen,
        ) {
            LocalNavGraph(
                snackbarHostState = snackbarHostState,
                navController = navController,
            )
        }
        navigation(
            startDestination = Online_MenuScreen,
            route = OnlineMenuScreen,
        ) {
            OnlineNavGraph(
                snackbarHostState = snackbarHostState,
                navController = navController,
            )
        }
    }
}
