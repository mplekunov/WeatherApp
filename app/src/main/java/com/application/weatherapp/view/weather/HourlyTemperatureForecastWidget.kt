package com.application.weatherapp.view.weather

import android.graphics.Paint
import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.application.weatherapp.model.graph.ValuePoint
import com.application.weatherapp.model.graph.calculateYCoordinate
import com.application.weatherapp.model.weather.HourlyWeather

@Composable
fun HourlyTemperatureForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather
) {
    var columnSize by remember { mutableStateOf(Size.Zero) }

    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
            val currentValue = weather.currentTemperature.value

            var nextValue =
                hourlyWeather.weatherForecast.first().currentTemperature.value

            if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                nextValue =
                    hourlyWeather.weatherForecast[index + 1].currentTemperature.value

            val prevValue =
                if (index == 0) hourlyWeather.weatherForecast.last().currentTemperature.value
                else hourlyWeather.weatherForecast[index - 1].currentTemperature.value

            Column(modifier = Modifier) {
                HourlyTemperatureCurve(
                    prevValue = prevValue,
                    currentValue = currentValue,
                    nextValue = nextValue,
                    maxValue = hourlyWeather.maxTemperature.value,
                    minValue = hourlyWeather.minTemperature.value,
                    canvasSize = Size(columnSize.width, 150F),
                )

                Column(modifier = Modifier
                    .padding(top = 150.dp)
                    .onGloballyPositioned {
                        columnSize = it.size.toSize()
                    }
                ) {
                    Icon(
                        painter = getWeatherIconPainter(weatherType = weather.weatherType),
                        contentDescription = weather.weatherDescription,
                        modifier = Modifier
                            .size(48.dp)
                    )

                    Text(
                        text = "${weather.date.hour}",
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}



@Composable
private fun HourlyTemperatureCurve(
    prevValue: Float,
    currentValue: Float,
    nextValue: Float,
    maxValue: Float,
    minValue: Float,
    canvasSize: Size,
    modifier: Modifier = Modifier
) {
    val startX = 0F
    val endX = startX + canvasSize.width
    val midX = (endX + startX) / 2

    val startPoint = ValuePoint(
        x = 0F,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (prevValue + currentValue) / 2,
            canvasSize.height
        ),
        value = (prevValue + currentValue) / 2
    )

    val controlPoint = ValuePoint(
        x = midX,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            currentValue,
            canvasSize.height
        ),
        value = currentValue
    )

    val endPoint = ValuePoint(
        x = endX,
        y = calculateYCoordinate(
            maxValue,
            minValue,
            (nextValue + currentValue) / 2,
            canvasSize.height
        ),
        value = (nextValue + currentValue) / 2
    )

    val graphGradientColors = listOf(
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary
    )

    val areaUnderCurveGradientColors = listOf(
        Color.Transparent,
        Color.Transparent
    )

    val borderGradientColors = listOf(
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f),
    )

    HourlyTemperatureAsQuadraticCurve(
        startPoint = startPoint,
        controlPoint = controlPoint,
        endPoint = endPoint,
        canvasSize = canvasSize,
        modifier = modifier,
        graphGradientColors = graphGradientColors,
        areaUnderCurveGradientColors = areaUnderCurveGradientColors,
        borderGradientColors = borderGradientColors,
        midLineGradientColors = borderGradientColors
    )
}

@Composable
private fun HourlyTemperatureAsQuadraticCurve(
    startPoint: ValuePoint,
    controlPoint: ValuePoint,
    endPoint: ValuePoint,
    canvasSize: Size,
    modifier: Modifier = Modifier,
    graphGradientColors: List<Color> = listOf(
        MaterialTheme.colorScheme.onPrimary,
        MaterialTheme.colorScheme.onPrimary
    ),
    areaUnderCurveGradientColors: List<Color> = listOf(
        Color.Transparent,
        Color.Transparent
    ),
    borderGradientColors: List<Color>,
    midLineGradientColors: List<Color>,
    color: Color = MaterialTheme.colorScheme.onPrimary
) {
    Canvas(modifier = modifier) {
        val filledPath = Path().apply {
            moveTo(startPoint.x, canvasSize.height.dp.toPx())

            lineTo(startPoint.x, startPoint.y)

            quadraticBezierTo(
                controlPoint.x, controlPoint.y,
                endPoint.x, endPoint.y
            )

            lineTo(endPoint.x, canvasSize.height.dp.toPx())

            close()
        }

        val graphPath = Path().apply {
            moveTo(startPoint.x, startPoint.y)

            quadraticBezierTo(
                controlPoint.x, controlPoint.y,
                endPoint.x, endPoint.y
            )
        }

        val pathMeasure = PathMeasure(graphPath.asAndroidPath(), false)
        val pos = FloatArray(2)

        pathMeasure.getPosTan(pathMeasure.length / 2, pos, null)

        val midY = pos[1]

        drawPath(
            path = graphPath,
            brush = Brush.verticalGradient(
                colors = graphGradientColors,
                startY = startPoint.y,
                endY = canvasSize.height.dp.toPx()
            ),
            style = Stroke(4F)
        )

        drawLine(
            start = Offset(startPoint.x, startPoint.y),
            end = Offset(startPoint.x, canvasSize.height.dp.toPx()),
            brush = Brush.verticalGradient(
                colors = borderGradientColors,
                startY = startPoint.y,
                endY = canvasSize.height.dp.toPx()
            )
        )

        drawLine(
            start = Offset(controlPoint.x, midY),
            end = Offset(controlPoint.x, canvasSize.height.dp.toPx()),
            brush = Brush.verticalGradient(
                colors = midLineGradientColors,
                startY = midY,
                endY = canvasSize.height.dp.toPx()
            ),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 10f), 20f)
        )

        drawLine(
            start = Offset(endPoint.x, endPoint.y),
            end = Offset(endPoint.x, canvasSize.height.dp.toPx()),
            brush = Brush.verticalGradient(
                colors = borderGradientColors,
                startY = endPoint.y,
                endY = canvasSize.height.dp.toPx()
            )
        )

        drawPath(
            path = filledPath,
            brush = Brush.verticalGradient(
                colors = areaUnderCurveGradientColors,
                startY = startPoint.y,
                endY = canvasSize.height.dp.toPx()
            ),
            style = Fill
        )


        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "${controlPoint.value.toInt()}",
                controlPoint.x,
                midY - 50,
                Paint().apply {
                    textSize = 34F
                    textAlign = Paint.Align.CENTER
                    this.color = color.toArgb()
                }
            )
        }
    }
}