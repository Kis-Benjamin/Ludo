package hu.bme.aut.android.ludocompose.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.ludocompose.features.game.GameScreen
import hu.bme.aut.android.ludocompose.features.loadgame.LoadGameScreen
import hu.bme.aut.android.ludocompose.features.menu.MenuScreen
import hu.bme.aut.android.ludocompose.features.newgame.NewGameScreen
import hu.bme.aut.android.ludocompose.features.savegame.SaveGameScreen
import hu.bme.aut.android.ludocompose.features.scoreboard.ScoreBoardScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    snackbarHostState: SnackbarHostState,
    onTitleChange: (Int) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    fun navigateTo(destination: Screen) {
        navController.navigate(destination.route) {
            popUpTo(Screen.Menu.route)
            launchSingleTop = true
        }
    }

    fun NavGraphBuilder.composable(
        screen: Screen,
        content: @Composable (NavBackStackEntry) -> Unit
    ) {
        composable(screen.route) {
            onTitleChange(screen.title)
            content(it)
        }
    }

    NavHost(
        navController = navController,
        startDestination = MenuScreen.route,
    ) {
        composable(MenuScreen) {
            MenuScreen(
                onNavigateToNewGame = { navigateTo(NewGameScreen) },
                onNavigateToLoadGame = { navigateTo(LoadGameScreen) },
                onNavigateToSaveGame = { navigateTo(SaveGameScreen) },
                onNavigateToGame = { navigateTo(GameScreen) },
                onNavigateToScoreboard = { navigateTo(ScoreBoardScreen) },
            )
        }
        composable(NewGameScreen) {
            NewGameScreen(
                snackbarHostState = snackbarHostState,
                onSuccess = { navigateTo(GameScreen) },
            )
        }
        composable(LoadGameScreen) {
            LoadGameScreen(
                snackbarHostState = snackbarHostState,
                onSuccess = { navigateTo(GameScreen) },
            )
        }
        dialog(SaveGameScreen.route) {
            SaveGameScreen(
                snackbarHostState = snackbarHostState,
                onSuccess = { navigateTo(MenuScreen) },
            )
        }
        composable(GameScreen) {
            GameScreen(
                onGameEnded = { navigateTo(MenuScreen) },
            )
        }
        composable(ScoreBoardScreen) {
            ScoreBoardScreen(
                snackbarHostState = snackbarHostState,
            )
        }
    }
}