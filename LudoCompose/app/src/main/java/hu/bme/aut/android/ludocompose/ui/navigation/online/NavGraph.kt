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

package hu.bme.aut.android.ludocompose.ui.navigation.online

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import hu.bme.aut.android.ludocompose.ui.features.online.game.GameScreen
import hu.bme.aut.android.ludocompose.ui.features.online.joinroom.JoinRoomScreen
import hu.bme.aut.android.ludocompose.ui.features.online.menu.MenuScreen
import hu.bme.aut.android.ludocompose.ui.features.online.newroom.NewRoomScreen
import hu.bme.aut.android.ludocompose.ui.features.online.scoreboard.ScoreBoardScreen
import hu.bme.aut.android.ludocompose.ui.navigation.common.LudoNavGraphBuilder
import hu.bme.aut.android.ludocompose.ui.navigation.common.Screen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
fun LudoNavGraphBuilder.NavGraph(
    snackbarHostState: SnackbarHostState,
    navController: NavController,
) {
    fun navigate(destination: Screen) {
        navController.navigate(destination, MenuScreen)
    }

    composable(MenuScreen) {
        MenuScreen(
            onNavigateToNewRoom = { navigate(NewRoomScreen) },
            onNavigateToJoinRoom = { navigate(JoinRoomScreen) },
            onNavigateToGame = { navigate(GameScreen) },
            onNavigateToScoreboard = { navigate(ScoreBoardScreen) },
        )
    }
    composable(NewRoomScreen) {
        NewRoomScreen()
    }
    composable(JoinRoomScreen) {
        JoinRoomScreen()
    }
    composable(GameScreen) {
        GameScreen()
    }
    composable(ScoreBoardScreen) {
        ScoreBoardScreen()
    }
}