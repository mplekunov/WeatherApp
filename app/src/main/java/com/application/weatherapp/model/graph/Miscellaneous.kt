package com.application.weatherapp.model.graph

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