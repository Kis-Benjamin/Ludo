package hu.bme.aut.android.ludocompose.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
            enterTransition = {
                fadeIn(
                    animationSpec = tween(300, easing = LinearEasing)
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(300, easing = LinearEasing)
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
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