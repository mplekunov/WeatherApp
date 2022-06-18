package com.application.weatherapp.view.ui.weather

import android.graphics.Paint
import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.ValuePoint
import com.application.weatherapp.model.graph.calculateYCoordinate
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Preview
@Composable
fun HourlyPrecipitationForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather = SampleHourlyWeatherProvider().values.first()
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val levelFontSize = 12.sp
    val canvasSize = Size(40F, 100F)

    Column(modifier = modifier) {
        Text(
            text = "Precipitation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyRow(
            modifier = Modifier.heightIn(max = 120.dp)
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                if (index == 0) {
                    Column(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "Heavy",
                            fontSize = levelFontSize,
                            color = fontColor,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Strong",
                            fontSize = levelFontSize,
                            color = fontColor,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Medium",
                            fontSize = levelFontSize,
                            color = fontColor,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Light",
                            fontSize = levelFontSize,
                            color = fontColor,
                            modifier = Modifier.weight(2f)
                        )
                    }
                }

                val currentValue = weather.precipitation.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().precipitation.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].precipitation.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().precipitation.value
                    else hourlyWeather.weatherForecast[index - 1].precipitation.value


                Column(modifier = Modifier) {
                    Box {
                        DrawPrecipitationLevelLine(
                            modifier = Modifier.padding(top = levelFontSize.value.dp),
                            canvasSize = canvasSize,
                            color = fontColor,
                            numOfLines = 4,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                        )

                        HourlyPrecipitationGraph(
                            prevValue = prevValue,
                            currentValue = currentValue,
                            nextValue = nextValue,
                            maxValue = 5F,
                            minValue = hourlyWeather.minPrecipitation.value,
                            canvasSize = canvasSize
                        )
                    }

                    Text(
                        text = "${weather.date.hour}",
                        fontSize = 12.sp,
                        color = fontColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawPrecipitationLevelLine(
    canvasSize: Size,
    color: Color,
    numOfLines: Int,
    modifier: Modifier = Modifier,
    pathEffect: PathEffect? = null
) {
    val lineHeight = canvasSize.height / numOfLines

    Canvas(modifier = modifier) {
        for (i in 0 until numOfLines) {
            drawLine(
                start = Offset(0F, (lineHeight * i).dp.toPx()),
                end = Offset(canvasSize.width.dp.toPx(), (lineHeight * i).dp.toPx()),
                color = color,
                pathEffect = pathEffect
            )
        }
    }
}

@Composable
fun HourlyPrecipitationGraph(
    prevValue: Float,
    currentValue: Float,
    nextValue: Float,
    maxValue: Float,
    minValue: Float,
    canvasSize: Size
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

    val graphColor = MaterialTheme.colorScheme.onPrimary

    HourlyPrecipitationAsQuadraticCurve(
        startPoint = startPoint,
        controlPoint = controlPoint,
        endPoint = endPoint,
        modifier = Modifier,
        canvasSize = canvasSize,
        graphColor = graphColor
    )
}

@Composable
private fun HourlyPrecipitationAsQuadraticCurve(
    startPoint: ValuePoint,
    controlPoint: ValuePoint,
    endPoint: ValuePoint,
    modifier: Modifier,
    canvasSize: Size,
    graphColor: Color = Color.White,
    fontColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Canvas(
        modifier = modifier
            .width(canvasSize.width.dp)
            .height(canvasSize.height.dp)
    ) {
        val graphPath = Path().apply {
            moveTo(startPoint.x.dp.toPx(), startPoint.y.dp.toPx())

            quadraticBezierTo(
                controlPoint.x.dp.toPx(), controlPoint.y.dp.toPx(),
                endPoint.x.dp.toPx(), endPoint.y.dp.toPx()
            )
        }

        val pathMeasure = PathMeasure(graphPath.asAndroidPath(), false)
        val pos = FloatArray(2)

        pathMeasure.getPosTan(pathMeasure.length / 2, pos, null)

        val midY = pos[1]

        drawPath(
            path = graphPath,
            color = graphColor,
            style = Stroke(4F)
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                String.format("%.1f", controlPoint.value),
                controlPoint.x.dp.toPx(),
                midY - 40,
                Paint().apply {
                    textSize = 34F
                    textAlign = Paint.Align.CENTER
                    this.color = fontColor.toArgb()
                }
            )
        }
    }
}
