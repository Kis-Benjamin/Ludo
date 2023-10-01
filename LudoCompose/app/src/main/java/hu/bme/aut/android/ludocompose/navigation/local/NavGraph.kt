package hu.bme.aut.android.ludocompose.navigation.local

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import hu.bme.aut.android.ludocompose.features.local.game.GameScreen
import hu.bme.aut.android.ludocompose.features.local.loadgame.LoadGameScreen
import hu.bme.aut.android.ludocompose.features.local.menu.MenuScreen
import hu.bme.aut.android.ludocompose.features.local.newgame.NewGameScreen
import hu.bme.aut.android.ludocompose.features.local.savegame.SaveGameScreen
import hu.bme.aut.android.ludocompose.features.local.scoreboard.ScoreBoardScreen
import hu.bme.aut.android.ludocompose.ui.animation.enterTransition
import hu.bme.aut.android.ludocompose.ui.animation.exitTransition

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onTitleChange: (Int) -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    fun navigateTo(destination: Screen) {
        navController.navigate(destination.route) {
            popUpTo(MenuScreen.route)
            launchSingleTop = true
        }
    }

    fun NavGraphBuilder.composable(
        screen: Screen,
        content: @Composable (NavBackStackEntry) -> Unit
    ) {
        composable(
            route = screen.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
        ) {
            onTitleChange(screen.title)
            content(it)
        }
    }

    NavHost(
        navController = navController,
        startDestination = MenuScreen.route,
        modifier = modifier,
        contentAlignment = Alignment.Center,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
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