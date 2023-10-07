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

package hu.bme.aut.android.ludocompose.navigation.main

import androidx.annotation.StringRes
import hu.bme.aut.android.ludocompose.R
import hu.bme.aut.android.ludocompose.navigation.common.Screen

data object MenuScreen : Screen("main_menu", R.string.menu_main) {
    override val enableNavigationBack: Boolean get() = false
}

data object LocalMenuScreen : Screen("local_menu", R.string.menu_local)

data object OnlineMenuScreen : Screen("online_menu", R.string.menu_online)

data object AboutScreen : Screen("about", R.string.menu_about)
