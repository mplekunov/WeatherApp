package com.application.weatherapp.view.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PopupComponentAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean,
    durationMills: Int,
    startHeight: Dp,
    content: @Composable
    (AnimatedVisibilityScope.() -> Unit)
) {
    val density = LocalDensity.current

    val enter = slideInVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) {
        with(density) { startHeight.roundToPx() }
    } + expandVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) + scaleIn(
        animationSpec = tween(durationMillis = durationMills)
    )

    val exit = slideOutVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) {
        with(density) { startHeight.roundToPx() }
    } + shrinkVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) + scaleOut(
        animationSpec = tween(durationMillis = durationMills)
    )

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        content =  content,
        enter = enter,
        exit = exit
    )
}