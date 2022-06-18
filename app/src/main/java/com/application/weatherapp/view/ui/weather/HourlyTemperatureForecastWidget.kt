package com.application.weatherapp.view.ui.weather

import android.graphics.Paint
import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Preview
@Composable
fun HourlyTemperatureForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather = SampleHourlyWeatherProvider().values.first()
) {
    val canvasSize = Size(40F, 190F)

    LazyRow(
        modifier = modifier.heightIn(max = 250.dp)
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

            val tripleValuePoint = getTripleValuePoint(
                startValue =  prevValue,
                midValue = currentValue,
                endValue =  nextValue,
                maxValue = hourlyWeather.maxTemperature.value,
                minValue = hourlyWeather.minTemperature.value,
                canvasSize = canvasSize
            )

            val brush =
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0f),
                    )
                )

            Column(modifier = Modifier) {
                Box {
                    DrawQuadraticCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = canvasSize,
                        graphColor = MaterialTheme.colorScheme.onPrimary
                    )

                    DrawAreaUnderQuadraticCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = canvasSize,
                        fillColor = Color.Transparent
                    )

                    DrawCurveSideBorders(
                        tupleValuePoint = tripleValuePoint,
                        canvasSize = canvasSize,
                        borderBrush = brush
                    )

                    DrawLineInMiddleOfCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = canvasSize,
                        midLineBrush = brush,
                        midLinePathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 10f), 20f)
                    )

                    DrawTextInMidOfCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = canvasSize,
                        fontColor = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Icon(
                    painter = getWeatherIconPainter(weatherType = weather.weatherType),
                    contentDescription = weather.weatherDescription,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(canvasSize.width.dp)
                )

                Text(
                    text = "${weather.date.hour}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}