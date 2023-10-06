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

package hu.bme.aut.android.ludocompose.navigation.local

import androidx.annotation.StringRes
import hu.bme.aut.android.ludocompose.R

sealed class Screen(val route: String, @StringRes val title: Int)
data object MenuScreen : Screen("menu", R.string.app_name)
data object NewGameScreen : Screen("new_game", R.string.menu_new_game)
data object LoadGameScreen : Screen("load_game", R.string.menu_load_game)
data object SaveGameScreen : Screen("save_game", R.string.menu_save_game)
data object GameScreen : Screen("game", R.string.app_name)
data object ScoreBoardScreen : Screen("score_board", R.string.menu_score_board)
