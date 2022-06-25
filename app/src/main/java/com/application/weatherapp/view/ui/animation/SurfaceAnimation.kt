package com.application.weatherapp.view.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SurfaceAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean,
    durationMills: Int,
    content: @Composable
    (AnimatedVisibilityScope.() -> Unit)
) {
    val density = LocalDensity.current

    val enter = slideInVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) {
        with(density) { 0.dp.roundToPx() }
    } + expandVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) + scaleIn(
        animationSpec = tween(durationMillis = durationMills)
    )

    val exit = slideOutVertically(
        animationSpec = tween(durationMillis = durationMills)
    ) {
        with(density) { 0.dp.roundToPx() }
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