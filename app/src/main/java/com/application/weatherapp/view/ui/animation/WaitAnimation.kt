package com.application.weatherapp.view.ui.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WaitAnimation() {
    val colorValue by rememberInfiniteTransition().animateColor(
        initialValue = Color.LightGray,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val backgroundColorValue by rememberInfiniteTransition().animateColor(
        initialValue = Color.Black,
        targetValue = Color.LightGray,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotationValue by rememberInfiniteTransition().animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        )
    )

    val sizeValue by rememberInfiniteTransition().animateValue(
        initialValue = 80.dp,
        targetValue = 0.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColorValue)
            .rotate(rotationValue),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .size(sizeValue)
                .background(colorValue)

        ) {}
    }
}