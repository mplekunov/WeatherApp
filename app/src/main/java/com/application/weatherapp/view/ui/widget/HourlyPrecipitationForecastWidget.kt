package com.application.weatherapp.view.ui.widget

import android.graphics.Paint
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.ui.theme.Typography
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode

@Preview
@Composable
private fun PreviewHourlyPrecipitationForecastWidget() {
    HourlyPrecipitationForecastWidget(
        graphSize = DpSize(60.dp, 100.dp),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyPrecipitationForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: DpSize,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val fontSize = (graphSize.width.value / 5).sp

    val density = LocalDensity.current

    Column(modifier = modifier) {
        Text(
            text = "Precipitation",
            style = Typography.titleMedium,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.precipitation.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().precipitation.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].precipitation.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().precipitation.value
                    else hourlyWeather.weatherForecast[index - 1].precipitation.value

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = 5F,
                    minValue = 0F,
                    canvasSize = graphSize
                )

                val colors = listOf(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                    Color.Transparent
                )

                val brush = Brush.verticalGradient(
                    colors = colors
                )

                if (index == 0) {
                    Box(
                        modifier = Modifier
                            .size(graphSize.width, graphSize.height)
                    ) {
                        DrawPrecipitationLevelLabel(
                            modifier = Modifier.offset(y = (fontSize / 2).value.dp),
                            canvasSize = graphSize,
                            text = listOf("Heavy", "Strong", "Medium", "Light"),
                            fontSize = fontSize,
                            fontColor = MaterialTheme.colorScheme.onPrimary
                        )

                        Canvas(modifier = Modifier) {
                            drawContext.canvas.nativeCanvas.apply {
                                drawText(
                                    "In ${hourlyWeather.weatherForecast.first().precipitation.unit.unit}",
                                    0F,
                                    graphSize.height.toPx(),
                                    Paint().apply {
                                        textSize = density.run { fontSize.toPx() }
                                        textAlign = Paint.Align.LEFT
                                        this.color = fontColor.toArgb()
                                    }
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier) {
                    Box {
                        DrawPrecipitationLevelLine(
                            modifier = Modifier,
                            canvasSize = graphSize,
                            lineColor = fontColor,
                            numOfLines = 4,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                        )

                        Box(modifier = Modifier.offset(y = (-20).dp)) {
                            DrawTextInMidOfCurve(
                                modifier = Modifier.offset(y = (-20).dp),
                                text = BigDecimal(currentValue.toString())
                                    .setScale(1, RoundingMode.HALF_UP)
                                    .stripTrailingZeros()
                                    .toPlainString(),
                                tripleValuePoint = tripleValuePoint,
                                canvasSize = graphSize,
                                fontColor = MaterialTheme.colorScheme.onPrimary,
                                fontSize = fontSize
                            )

                            DrawQuadraticCurve(
                                modifier = Modifier,
                                tripleValuePoint = tripleValuePoint,
                                canvasSize = graphSize,
                                graphColor = MaterialTheme.colorScheme.onPrimary
                            )

                            DrawCurveSideBorders(
                                tupleValuePoint = tripleValuePoint,
                                canvasSize = graphSize,
                                borderBrush = brush
                            )
                        }
                    }

                    Text(
                        text = "${weather.date.hour}",
                        fontSize = fontSize,
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
private fun DrawPrecipitationLevelLine(
    canvasSize: DpSize,
    lineColor: Color,
    numOfLines: Int,
    modifier: Modifier = Modifier,
    pathEffect: PathEffect? = null
) {
    val lineHeight = canvasSize.height / numOfLines

    Canvas(
        modifier = modifier
            .size(canvasSize.width, canvasSize.width)
    ) {
        for (i in 0 until numOfLines) {
            drawLine(
                start = Offset(0F.dp.toPx(), (lineHeight * i).toPx()),
                end = Offset(canvasSize.width.toPx(), (lineHeight * i).toPx()),
                color = lineColor,
                pathEffect = pathEffect
            )
        }
    }
}

@Composable
private fun DrawPrecipitationLevelLabel(
    modifier: Modifier = Modifier,
    canvasSize: DpSize,
    text: List<String>,
    fontColor: Color,
    fontSize: TextUnit
) {
    val lineHeight = canvasSize.height / text.size
    val density = LocalDensity.current

    Canvas(modifier = modifier) {
        for (i in text.indices) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    text[i],
                    0F,
                    (lineHeight * i).toPx(),
                    Paint().apply {
                        textSize = density.run { fontSize.toPx() }
                        textAlign = Paint.Align.LEFT
                        this.color = fontColor.toArgb()
                    }
                )
            }
        }
    }
}