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

package hu.bme.aut.android.ludocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.ludocompose.navigation.common.Screen
import hu.bme.aut.android.ludocompose.navigation.menu.MenuScreen
import hu.bme.aut.android.ludocompose.navigation.menu.NavGraph
import hu.bme.aut.android.ludocompose.navigation.local.NavGraph as LocalNavGraph
import hu.bme.aut.android.ludocompose.ui.common.LudoAppBar
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme

@AndroidEntryPoint
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LudoComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background,
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }

                    var screen: Screen by remember { mutableStateOf(MenuScreen) }

                    val navController = rememberNavController()

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            LudoAppBar(
                                title = stringResource(screen.title),
                                enableNavigationBack = screen.enableNavigationBack,
                                onNavigationClick = {
                                    navController.popBackStack()
                                },
                            )
                        },
                        floatingActionButton = {}
                    ) { paddingValues ->
                        NavGraph(
                            snackbarHostState = snackbarHostState,
                            onNavigate = { screen = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}
