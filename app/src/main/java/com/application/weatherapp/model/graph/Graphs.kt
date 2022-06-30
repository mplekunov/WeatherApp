package com.application.weatherapp.model.graph

import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun DrawQuadraticCurve(
    modifier: Modifier = Modifier,
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    graphColor: Color,
    graphStyle: DrawStyle = Stroke(4F)
) {
    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    Canvas(
        modifier = modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawPath(
            path = graphPath,
            color = graphColor,
            style = graphStyle
        )
    }
}

@Composable
fun DrawQuadraticCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    graphBrush: Brush,
    graphStyle: DrawStyle = Stroke(4F)
) {
    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawPath(
            path = graphPath,
            brush = graphBrush,
            style = graphStyle
        )
    }
}

@Composable
fun DrawCubicCurve(
    modifier: Modifier = Modifier,
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    graphColor: Color,
    graphStyle: DrawStyle = Stroke(4F)
) {
    val graphPath =
        getCubicCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    Canvas(
        modifier = modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawPath(
            path = graphPath,
            color = graphColor,
            style = graphStyle
        )
    }
}

@Composable
private fun getCubicCurvePath(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    isFilled: Boolean = false
): Path {
    val startPointX = tripleValuePoint.startPoint.x
    val startPointY = tripleValuePoint.startPoint.y

    val midPointX = tripleValuePoint.midPoint.x

    val endPointX = tripleValuePoint.endPoint.x
    val endPointY = tripleValuePoint.endPoint.y

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    val firstControlPointX = (startPointX + midPointX) / 2
    val firstControlPointY = startPointY

    val secondControlPointX = (midPointX + endPointX) / 2
    val secondControlPointY = endPointY

    return when (isFilled) {
        true -> Path().apply {
            moveTo(startPointX, canvasHeight)

            lineTo(startPointX, startPointY)

            cubicTo(
                firstControlPointX, firstControlPointY,
                secondControlPointX, secondControlPointY,
                endPointX, endPointY
            )

            lineTo(endPointX, canvasHeight)

            close()
        }
        false -> Path().apply {
            moveTo(startPointX, startPointY)

            cubicTo(
                firstControlPointX, firstControlPointY,
                secondControlPointX, secondControlPointY,
                endPointX, endPointY
            )
        }
    }
}

@Composable
private fun getQuadraticCurvePath(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    isFilled: Boolean = false
): Path {
    val startPointX = tripleValuePoint.startPoint.x
    val startPointY = tripleValuePoint.startPoint.y

    val controlPointX = tripleValuePoint.midPoint.x
    val controlPointY = tripleValuePoint.midPoint.y

    val endPointX = tripleValuePoint.endPoint.x
    val endPointY = tripleValuePoint.endPoint.y

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    return when (isFilled) {
        true -> Path().apply {
            moveTo(startPointX, canvasHeight)

            lineTo(startPointX, startPointY)

            quadraticBezierTo(
                controlPointX, controlPointY,
                endPointX, endPointY
            )

            lineTo(endPointX, canvasHeight)

            close()
        }
        false -> Path().apply {
            moveTo(startPointX, startPointY)

            quadraticBezierTo(
                controlPointX, controlPointY,
                endPointX, endPointY
            )
        }
    }
}

@Composable
fun DrawAreaUnderQuadraticCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    fillBrush: Brush
) {
    val filledPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize,
            isFilled = true
        )

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawPath(
            path = filledPath,
            brush = fillBrush,
            style = Fill
        )
    }
}

@Composable
fun DrawAreaUnderQuadraticCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    fillColor: Color
) {
    val filledPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize,
            isFilled = true
        )

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawPath(
            path = filledPath,
            color = fillColor,
            style = Fill
        )
    }
}

@Composable
fun DrawCurveSideBorders(
    tupleValuePoint: TupleValuePoint,
    canvasSize: DpSize,
    borderColor: Color
) {
    val startPointX = tupleValuePoint.startPoint.x
    val startPointY = tupleValuePoint.startPoint.y

    val endPointX = tupleValuePoint.endPoint.x
    val endPointY = tupleValuePoint.endPoint.y

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawLine(
            start = Offset(startPointX, startPointY),
            end = Offset(startPointX, canvasHeight),
            color = borderColor
        )

        drawLine(
            start = Offset(endPointX, endPointY),
            end = Offset(endPointX, canvasHeight),
            color = borderColor
        )
    }
}

@Composable
fun DrawCurveSideBorders(
    tupleValuePoint: TupleValuePoint,
    canvasSize: DpSize,
    borderBrush: Brush
) {
    val startPointX = tupleValuePoint.startPoint.x
    val startPointY = tupleValuePoint.startPoint.y

    val endPointX = tupleValuePoint.endPoint.x
    val endPointY = tupleValuePoint.endPoint.y

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawLine(
            start = Offset(startPointX, startPointY),
            end = Offset(startPointX, canvasHeight),
            brush = borderBrush
        )

        drawLine(
            start = Offset(endPointX, endPointY),
            end = Offset(endPointX, canvasHeight),
            brush = borderBrush
        )
    }
}

@Composable
fun DrawLineInMiddleOfCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    midLineBrush: Brush,
    midLinePathEffect: PathEffect? = null
) {
    val controlPointX = tripleValuePoint.midPoint.x

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    val pathMeasure = PathMeasure(graphPath.asAndroidPath(), false)
    val pos = FloatArray(2)

    pathMeasure.getPosTan(pathMeasure.length / 2, pos, null)

    val midY = pos[1]

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawLine(
            start = Offset(controlPointX, midY),
            end = Offset(controlPointX, canvasHeight),
            brush = midLineBrush,
            pathEffect = midLinePathEffect
        )
    }
}

@Composable
fun DrawLineInMiddleOfCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    midLineColor: Color,
    midLinePathEffect: PathEffect? = null
) {
    val controlPointX = tripleValuePoint.midPoint.x

    val canvasHeight = LocalDensity.current.run { canvasSize.height.toPx() }

    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    val pathMeasure = PathMeasure(graphPath.asAndroidPath(), false)
    val pos = FloatArray(2)

    pathMeasure.getPosTan(pathMeasure.length / 2, pos, null)

    val midY = pos[1]

    Canvas(
        modifier = Modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawLine(
            start = Offset(controlPointX, midY),
            end = Offset(controlPointX, canvasHeight),
            color = midLineColor,
            pathEffect = midLinePathEffect
        )
    }
}

@Composable
fun DrawTextInMidOfCurve(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    tripleValuePoint: TripleValuePoint,
    canvasSize: DpSize,
    fontColor: Color
) {
    val controlPointX = tripleValuePoint.midPoint.x

    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    val pathMeasure = PathMeasure(graphPath.asAndroidPath(), false)
    val pos = FloatArray(2)

    pathMeasure.getPosTan(pathMeasure.length / 2, pos, null)

    val midY = pos[1]

    Canvas(
        modifier = modifier
            .size(canvasSize.width, canvasSize.height)
    ) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                text,
                controlPointX,
                midY,
                android.graphics.Paint().apply {
                    textSize = density.run { fontSize.toPx() }
                    textAlign = android.graphics.Paint.Align.CENTER
                    this.color = fontColor.toArgb()
                }
            )
        }
    }
}