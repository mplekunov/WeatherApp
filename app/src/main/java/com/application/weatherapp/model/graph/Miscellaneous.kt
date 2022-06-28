package com.application.weatherapp.model.graph

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import kotlin.math.abs

fun calculateYCoordinate(
    maxValue: Float,
    minValue: Float,
    currentValue: Float,
    canvasHeight: Float
): Float {
    val step = canvasHeight / (abs(maxValue) + abs(minValue))

    return canvasHeight - currentValue * step
}

fun getTupleValuePoint(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: Size
) : TupleValuePoint {
    val startX = 0.dp
    val endX = startX + canvasSize.width.dp

    val startPoint = ValuePoint(
        x = 0.dp.value,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (startValue + midValue) / 2,
            canvasSize.height
        ),
        value = (startValue + midValue) / 2
    )

    val endPoint = ValuePoint(
        x = endX.value,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (endValue + midValue) / 2,
            canvasSize.height
        ),
        value = (endValue + midValue) / 2
    )

    return TupleValuePoint(startPoint, endPoint)
}

fun convertToQuadraticConnectionPoints(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: Size
) : TripleValuePoint {
    val startX = 0.dp
    val endX = startX + canvasSize.width.dp
    val midX = (endX + startX) / 2

    val tupleValuePoint = getTupleValuePoint(
        startValue = startValue,
        endValue = endValue,
        minValue = minValue,
        maxValue = maxValue,
        canvasSize = canvasSize,
        midValue = midValue
    )

    val controlPoint = ValuePoint(
        x = midX.value,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            midValue,
            canvasSize.height
        ),
        value = midValue
    )

    val startPoint = tupleValuePoint.startPoint

    val endPoint = tupleValuePoint.endPoint

    return TripleValuePoint(startPoint, controlPoint, endPoint)
}