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

package hu.bme.aut.android.ludocompose.ui.common

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.navigation.NavBackStackEntry

private const val DURATION = 500
private const val DELAY = 0
private val easing: Easing = EaseInOutSine

private val animationSpecFloat = tween<Float>(
    durationMillis = DURATION,
    delayMillis = DELAY,
    easing = easing
)

private val animationSpecIntOffset = tween<IntOffset>(
    durationMillis = DURATION,
    delayMillis = DELAY,
    easing = easing
)

private val animationSpecIntSize = tween<IntSize>(
    durationMillis = DURATION,
    delayMillis = DELAY,
    easing = easing
)

private val fadeIn: EnterTransition = fadeIn(animationSpecFloat)
private val fadeOut: ExitTransition = fadeOut(animationSpecFloat)

private fun <T> AnimatedContentTransitionScope<T>.slideIntoContainer(): EnterTransition =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End,
        animationSpecIntOffset
    )

private fun <T> AnimatedContentTransitionScope<T>.slideOutOfContainer(): ExitTransition =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpecIntOffset
    )

val transitionSpec: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
    fadeIn + slideIntoContainer() togetherWith fadeOut + slideOutOfContainer()
}

val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
    fadeIn + slideIntoContainer()
}

val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
    fadeOut + slideOutOfContainer()
}

val topEnterTransition: EnterTransition =
    fadeIn + expandVertically(
        animationSpecIntSize,
        expandFrom = Alignment.Top
    )

val topExitTransition: ExitTransition =
    shrinkVertically(
        animationSpecIntSize,
        shrinkTowards = Alignment.Top
    )

val startEnterTransition: EnterTransition =
    fadeIn + expandHorizontally(
        animationSpecIntSize,
        expandFrom = Alignment.Start
    )

val startExitTransition: ExitTransition =
    shrinkHorizontally(
        animationSpecIntSize,
        shrinkTowards = Alignment.Start
    )
