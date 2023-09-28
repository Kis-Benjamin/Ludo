package hu.bme.aut.android.ludocompose.navigation

import androidx.annotation.StringRes
import hu.bme.aut.android.ludocompose.R

sealed class Screen(val route: String, @StringRes val title: Int)
data object MenuScreen : Screen("menu", R.string.app_name)
data object NewGameScreen : Screen("new_game", R.string.menu_new_game)
data object LoadGameScreen : Screen("load_game", R.string.menu_load_game)
data object SaveGameScreen : Screen("save_game", R.string.menu_save_game)
data object GameScreen : Screen("game", R.string.app_name)
data object ScoreBoardScreen : Screen("score_board", R.string.menu_score_board)
