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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.ludocompose.navigation.NavGraph
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

                    var titleId by remember { mutableIntStateOf(R.string.app_name) }

                    Scaffold(
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            LudoAppBar(
                                title = stringResource(titleId),
                            )
                        },
                        floatingActionButton = {}
                    ) { paddingValues ->
                        NavGraph(
                            snackbarHostState = snackbarHostState,
                            onTitleChange = { titleId = it },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        )
                    }
                }
            }
        }
    }
}
