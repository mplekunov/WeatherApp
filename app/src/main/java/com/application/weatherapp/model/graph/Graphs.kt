package com.application.weatherapp.model.graph

import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun DrawQuadraticCurve(
    tripleValuePoint: TripleValuePoint,
    canvasSize: Size,
    graphColor: Color,
    graphStyle: DrawStyle = Stroke(4F)
) {
    val graphPath =
        getQuadraticCurvePath(
            tripleValuePoint = tripleValuePoint,
            canvasSize = canvasSize
        )

    Canvas(
        modifier = Modifier
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
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
            .size(canvasSize.width.dp, canvasSize.height.dp)
    ) {
        drawPath(
            path = graphPath,
            brush = graphBrush,
            style = graphStyle
        )
    }
}

@Composable
private fun getQuadraticCurvePath(
    tripleValuePoint: TripleValuePoint,
    canvasSize: Size,
    isFilled: Boolean = false
): Path {
    val startPointX = LocalDensity.current.run { tripleValuePoint.startPoint.x.dp.toPx() }
    val startPointY = LocalDensity.current.run { tripleValuePoint.startPoint.y.dp.toPx() }

    val controlPointX = LocalDensity.current.run { tripleValuePoint.midPoint.x.dp.toPx() }
    val controlPointY = LocalDensity.current.run { tripleValuePoint.midPoint.y.dp.toPx() }

    val endPointX = LocalDensity.current.run { tripleValuePoint.endPoint.x.dp.toPx() }
    val endPointY = LocalDensity.current.run { tripleValuePoint.endPoint.y.dp.toPx() }

//    val canvasWidth = LocalDensity.current.run { canvasSize.width.dp.toPx() }
//    val canvasHeight = LocalDensity.current.run { canvasSize.height.dp.toPx() }

    return when (isFilled) {
        true -> Path().apply {
            moveTo(startPointX, Float.MAX_VALUE)

            lineTo(startPointX, startPointY)

            quadraticBezierTo(
                controlPointX, controlPointY,
                endPointX, endPointY
            )

            lineTo(endPointX, Float.MAX_VALUE)

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
    canvasSize: Size,
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
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
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
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
    borderColor: Color
) {
    val startPointX = LocalDensity.current.run { tupleValuePoint.startPoint.x.dp.toPx() }
    val startPointY = LocalDensity.current.run { tupleValuePoint.startPoint.y.dp.toPx() }

    val endPointX = LocalDensity.current.run { tupleValuePoint.endPoint.x.dp.toPx() }
    val endPointY = LocalDensity.current.run { tupleValuePoint.endPoint.y.dp.toPx() }

//    val canvasWidth = LocalDensity.current.run { canvasSize.width.dp.toPx() }
    val canvasHeight = LocalDensity.current.run { canvasSize.height.dp.toPx() }

    Canvas(
        modifier = Modifier
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
    borderBrush: Brush
) {
    val startPointX = LocalDensity.current.run { tupleValuePoint.startPoint.x.dp.toPx() }
    val startPointY = LocalDensity.current.run { tupleValuePoint.startPoint.y.dp.toPx() }

    val endPointX = LocalDensity.current.run { tupleValuePoint.endPoint.x.dp.toPx() }
    val endPointY = LocalDensity.current.run { tupleValuePoint.endPoint.y.dp.toPx() }

//    val canvasWidth = LocalDensity.current.run { canvasSize.width.dp.toPx() }
    val canvasHeight = LocalDensity.current.run { canvasSize.height.dp.toPx() }

    Canvas(
        modifier = Modifier
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
    midLineBrush: Brush,
    midLinePathEffect: PathEffect? = null
) {
    val controlPointX = LocalDensity.current.run { tripleValuePoint.midPoint.x.dp.toPx() }

    val canvasHeight = LocalDensity.current.run { canvasSize.height.dp.toPx() }

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
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    canvasSize: Size,
    midLineColor: Color,
    midLinePathEffect: PathEffect? = null
) {
    val controlPointX = LocalDensity.current.run { tripleValuePoint.midPoint.x.dp.toPx() }

    val canvasHeight = LocalDensity.current.run { canvasSize.height.dp.toPx() }

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
            .size(canvasSize.width.dp, canvasSize.height.dp)
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
    tripleValuePoint: TripleValuePoint,
    canvasSize: Size,
    fontColor: Color
) {
    val controlPoint = tripleValuePoint.midPoint

    val controlPointX = LocalDensity.current.run { tripleValuePoint.midPoint.x.dp.toPx() }

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
            .size(canvasSize.width.dp, canvasSize.height.dp)
    ) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                String.format("%.1f", controlPoint.value),
                controlPointX,
                midY,
                android.graphics.Paint().apply {
                    textSize = 34F
                    textAlign = android.graphics.Paint.Align.CENTER
                    this.color = fontColor.toArgb()
                }
            )
        }
    }
}