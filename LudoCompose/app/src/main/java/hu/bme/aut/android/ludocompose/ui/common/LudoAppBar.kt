package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hu.bme.aut.android.ludocompose.ui.theme.LudoComposeTheme

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun LudoAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            AnimatedContent(
                targetState = title,
                transitionSpec = {
                    fadeIn(tween(700)) +
                            expandHorizontally(
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = LinearEasing
                                )
                            ) togetherWith
                            fadeOut(tween(700)) +
                            shrinkHorizontally(tween(700))
                },
                label = "TitleChangeAnimation"
            ) {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {},
        actions = {},
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun LudoAppBar_Preview() {
    LudoComposeTheme {
        LudoAppBar(
            title = "Ludo",
        )
    }
}
