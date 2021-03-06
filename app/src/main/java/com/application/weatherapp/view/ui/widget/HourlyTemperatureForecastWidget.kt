package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.view.ui.icon.getWeatherIconPainter
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode

@Preview
@Composable
private fun PreviewHourlyTemperatureForecastWidget() {
    HourlyTemperatureForecastWidget(
        graphSize = DpSize(90.dp, 200.dp),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyTemperatureForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: DpSize,
    hourlyWeather: HourlyWeather
) {
    val fontSize = (graphSize.width.value / 5).sp

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

            val tripleValuePoint = convertToQuadraticConnectionPoints(
                startValue = prevValue,
                midValue = currentValue,
                endValue = nextValue,
                maxValue = hourlyWeather.maxTemperature.value,
                minValue = hourlyWeather.minTemperature.value,
                canvasSize = graphSize
            )

            val brush =
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                        Color.Transparent,
                    )
                )

            Column(modifier = Modifier) {
                Box {

                    DrawTextInMidOfCurve(
                        modifier = Modifier.offset(y = (-20).dp),
                        tripleValuePoint = tripleValuePoint,
                        text = BigDecimal(currentValue.toString())
                            .setScale(1, RoundingMode.HALF_UP)
                            .stripTrailingZeros()
                            .toPlainString(),
                        canvasSize = graphSize,
                        fontColor = MaterialTheme.colorScheme.onPrimary,
                        fontSize = fontSize
                    )

                    DrawQuadraticCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = graphSize,
                        graphColor = MaterialTheme.colorScheme.onPrimary
                    )

                    DrawCurveSideBorders(
                        tupleValuePoint = tripleValuePoint,
                        canvasSize = graphSize,
                        borderBrush = brush
                    )

                    DrawLineInMiddleOfCurve(
                        tripleValuePoint = tripleValuePoint,
                        canvasSize = graphSize,
                        midLineBrush = brush,
                        midLinePathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(2f, 10f),
                            20f
                        )
                    )
                }

                Icon(
                    painter = getWeatherIconPainter(weatherType = weather.weatherType),
                    contentDescription = weather.weatherDescription,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "${weather.date.hour}",
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}