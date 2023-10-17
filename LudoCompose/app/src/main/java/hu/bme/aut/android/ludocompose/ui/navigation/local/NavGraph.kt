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

package hu.bme.aut.android.ludocompose.ui.navigation.local

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import hu.bme.aut.android.ludocompose.ui.features.local.game.GameScreen
import hu.bme.aut.android.ludocompose.ui.features.local.loadgame.LoadGameScreen
import hu.bme.aut.android.ludocompose.ui.features.local.menu.MenuScreen
import hu.bme.aut.android.ludocompose.ui.features.local.newgame.NewGameScreen
import hu.bme.aut.android.ludocompose.ui.features.local.savegame.SaveGameScreen
import hu.bme.aut.android.ludocompose.ui.features.local.scoreboard.ScoreBoardScreen
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
            onNavigateToNewGame = { navigate(NewGameScreen) },
            onNavigateToLoadGame = { navigate(LoadGameScreen) },
            onNavigateToSaveGame = { navigate(SaveGameScreen) },
            onNavigateToGame = { navigate(LocalGameScreen) },
            onNavigateToScoreboard = { navigate(ScoreBoardScreen) },
        )
    }
    composable(NewGameScreen) {
        NewGameScreen(
            snackbarHostState = snackbarHostState,
            onSuccess = { navigate(LocalGameScreen) },
        )
    }
    composable(LoadGameScreen) {
        LoadGameScreen(
            snackbarHostState = snackbarHostState,
            onSuccess = { navigate(LocalGameScreen) },
        )
    }
    dialog(SaveGameScreen) {
        SaveGameScreen(
            snackbarHostState = snackbarHostState,
            onSuccess = { navigate(MenuScreen) },
        )
    }
    composable(LocalGameScreen) {
        GameScreen(
            onGameEnded = { navigate(MenuScreen) },
        )
    }
    composable(ScoreBoardScreen) {
        ScoreBoardScreen(
            snackbarHostState = snackbarHostState,
        )
    }
}