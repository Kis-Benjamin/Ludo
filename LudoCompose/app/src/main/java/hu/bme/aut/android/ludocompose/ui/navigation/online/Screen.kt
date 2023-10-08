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

import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.ui.navigation.common.Screen

data object MenuScreen : Screen("online_menu", R.string.menu_online)

data object NewRoomScreen : Screen("new_room", R.string.menu_new_room)

data object JoinRoomScreen : Screen("join_room", R.string.menu_join_room)

data object GameScreen : Screen("online_game", R.string.menu_game)

data object ScoreBoardScreen : Screen("online_score_board", R.string.menu_score_board)
