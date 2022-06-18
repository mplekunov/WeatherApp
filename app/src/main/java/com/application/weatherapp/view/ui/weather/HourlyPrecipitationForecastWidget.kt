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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Preview
@Composable
fun HourlyPrecipitationForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather = SampleHourlyWeatherProvider().values.first()
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val canvasSize = Size(40F, 100F)

    Column(modifier = modifier) {
        Text(
            text = "Precipitation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyRow(
            modifier = Modifier.heightIn(max = 150.dp)
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

                val tripleValuePoint = getTripleValuePoint(
                    startValue =  prevValue,
                    midValue = currentValue,
                    endValue =  nextValue,
                    maxValue = hourlyWeather.maxPrecipitation.value,
                    minValue = hourlyWeather.minPrecipitation.value,
                    canvasSize = canvasSize
                )

                if (index == 0) {
                    DrawPrecipitationLevelLabel(
                        canvasSize = canvasSize,
                        text = listOf("Heavy", "Strong", "Medium", "Light"),
                        fontSize = 34F,
                        fontColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 50.dp)
                    )
                }

                Column(modifier = Modifier) {
                    Box {
                        DrawPrecipitationLevelLine(
                            modifier = Modifier,
                            canvasSize = canvasSize,
                            lineColor = fontColor,
                            numOfLines = 4,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                        )

                        DrawQuadraticCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = canvasSize,
                            graphColor = MaterialTheme.colorScheme.onPrimary
                        )

                        DrawTextInMidOfCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = canvasSize,
                            fontColor = MaterialTheme.colorScheme.onPrimary
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
    lineColor: Color,
    numOfLines: Int,
    modifier: Modifier = Modifier,
    pathEffect: PathEffect? = null
) {
    val lineHeight = canvasSize.height / numOfLines

    Canvas(modifier = modifier) {
        for (i in 0 until numOfLines) {
            drawLine(
                start = Offset(0F.dp.toPx(), (lineHeight * i).dp.toPx()),
                end = Offset(canvasSize.width.dp.toPx(), (lineHeight * i).dp.toPx()),
                color = lineColor,
                pathEffect = pathEffect
            )
        }
    }
}

@Composable
fun DrawPrecipitationLevelLabel(
    modifier: Modifier = Modifier,
    canvasSize: Size,
    text: List<String>,
    fontColor: Color,
    fontSize: Float
) {
    val lineHeight = canvasSize.height / text.size

    Canvas(modifier = modifier) {
        for (i in text.indices) {
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    text[i],
                    0F.dp.toPx(),
                    (lineHeight * i).dp.toPx(),
                    Paint().apply {
                        textSize = fontSize
                        textAlign = Paint.Align.LEFT
                        this.color = fontColor.toArgb()
                    }
                )
            }
        }
    }
}