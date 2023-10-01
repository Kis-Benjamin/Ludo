package hu.bme.aut.android.ludocompose.ui.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

private const val DURATION = 500

private val animationSpecFloat = tween<Float>(
    durationMillis = DURATION,
    delayMillis = 0,
    easing = FastOutSlowInEasing
)

private val animationSpecIntOffset = tween<IntOffset>(
    durationMillis = DURATION,
    delayMillis = 0,
    easing = FastOutSlowInEasing
)

private val transformOrigin = TransformOrigin(0.0f, 0.5f)

val transitionSpec: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
    fadeIn(
        animationSpecFloat
    ) + scaleIn(
        animationSpecFloat,
        initialScale = 0.0f,
        transformOrigin
    ) togetherWith fadeOut(
        animationSpecFloat
    ) + scaleOut(
        animationSpecFloat,
        targetScale = 0.0f,
        transformOrigin
    )
}

val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    fadeIn(
        animationSpecFloat
    ) + slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End,
        animationSpecIntOffset
    )
}


val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    fadeOut(
        animationSpecFloat
    ) + slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpecIntOffset
    )
}
