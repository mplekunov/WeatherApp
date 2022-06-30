package com.application.weatherapp.model.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun calculateYCoordinate(
    maxValue: Float,
    minValue: Float,
    currentValue: Float,
    canvasHeight: Float
): Float {
    val current = if (currentValue > maxValue) maxValue else currentValue

    val step = canvasHeight / (abs(maxValue) + abs(minValue))

    return canvasHeight - current * step
}

@Composable
fun getTupleValuePoint(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: DpSize
) : TupleValuePoint {
    val startX = LocalDensity.current.run { 0f.toDp().toPx() }
    val endX = startX + LocalDensity.current.run { canvasSize.width.toPx() }

    val height = LocalDensity.current.run { canvasSize.height.toPx() }

    val startPoint = ValuePoint(
        x = startX,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (startValue + midValue) / 2,
            height
        ),
        value = (startValue + midValue) / 2
    )

    val endPoint = ValuePoint(
        x = endX,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (endValue + midValue) / 2,
            height
        ),
        value = (endValue + midValue) / 2
    )

    return TupleValuePoint(startPoint, endPoint)
}

@Composable
fun convertToQuadraticConnectionPoints(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: DpSize
) : TripleValuePoint {
    val startX = LocalDensity.current.run { 0f.toDp().toPx() }
    val endX = startX + LocalDensity.current.run { canvasSize.width.toPx() }
    val midX = (endX + startX) / 2

    val height = LocalDensity.current.run { canvasSize.height.toPx() }

    val tupleValuePoint = getTupleValuePoint(
        startValue = startValue,
        endValue = endValue,
        minValue = minValue,
        maxValue = maxValue,
        canvasSize = canvasSize,
        midValue = midValue
    )

    val controlPoint = ValuePoint(
        x = midX,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            midValue,
            height
        ),
        value = midValue
    )

    val startPoint = tupleValuePoint.startPoint

    val endPoint = tupleValuePoint.endPoint

    return TripleValuePoint(startPoint, controlPoint, endPoint)
}