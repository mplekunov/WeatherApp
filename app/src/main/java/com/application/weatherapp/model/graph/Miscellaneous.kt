package com.application.weatherapp.model.graph

import androidx.compose.ui.geometry.Size
import kotlin.math.max

fun calculateYCoordinate(
    maxValue: Float,
    minValue: Float,
    currentValue: Float,
    canvasHeight: Float
): Float {
    val realValue = (maxValue - currentValue)

    val distanceOnCanvasPerValue = (canvasHeight / (maxValue))

    return realValue * distanceOnCanvasPerValue
}

fun getTupleValuePoint(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: Size
) : TupleValuePoint {
    val startX = 0F
    val endX = startX + canvasSize.width

    val startPoint = ValuePoint(
        x = 0F,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (startValue + midValue) / 2,
            canvasSize.height
        ),
        value = (startValue + midValue) / 2
    )

    val endPoint = ValuePoint(
        x = endX,
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

fun getTripleValuePoint(
    startValue: Float,
    midValue: Float,
    endValue: Float,
    minValue: Float,
    maxValue: Float,
    canvasSize: Size
) : TripleValuePoint {
    val startX = 0F
    val endX = startX + canvasSize.width
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
        x = midX,
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