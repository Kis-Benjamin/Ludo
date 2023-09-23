package hu.bme.aut.android.ludocompose.navigation

import androidx.annotation.StringRes
import hu.bme.aut.android.ludocompose.R

sealed class Screen(val route: String, @StringRes val title: Int) {
    object Menu : Screen("menu", R.string.app_name)
    object NewGame : Screen("new_game", R.string.menu_new_game)
    object LoadGame : Screen("load_game", R.string.menu_load_game)
    object SaveGame : Screen("save_game", R.string.menu_save_game)
    object Game : Screen("game", R.string.app_name)
    object ScoreBoard : Screen("score_board", R.string.menu_score_board)
}
