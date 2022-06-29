package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.DrawQuadraticCurve
import com.application.weatherapp.model.graph.DrawTextInMidOfCurve
import com.application.weatherapp.model.graph.convertToQuadraticConnectionPoints
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@Preview
@Composable
private fun PreviewHourlyCloudCoverForecastWidget() {
    HourlyCloudCoverForecastWidget(
        graphSize = Size(40F, 100F),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyCloudCoverForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: Size,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary

    Column(modifier = modifier) {
        Text(
            text = "Cloud cover",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.cloudCover.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().cloudCover.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].cloudCover.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().cloudCover.value
                    else hourlyWeather.weatherForecast[index - 1].cloudCover.value

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = hourlyWeather.maxCloudCover.value,
                    minValue = hourlyWeather.maxCloudCover.value,
                    canvasSize = graphSize
                )

                Column(modifier = Modifier) {
                    Box {
                        Box(modifier = Modifier.padding(top = 20.dp)) {
                            DrawQuadraticCurve(
                                tripleValuePoint = tripleValuePoint,
                                canvasSize = graphSize,
                                graphColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        DrawTextInMidOfCurve(
                            tripleValuePoint = tripleValuePoint,
                            text = BigDecimal(currentValue.toString())
                                .setScale(0, RoundingMode.HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString() + " ${weather.cloudCover.unit.unit}",
                            canvasSize = graphSize,
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