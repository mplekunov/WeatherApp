package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.DrawQuadraticCurve
import com.application.weatherapp.model.graph.DrawTextInMidOfCurve
import com.application.weatherapp.model.graph.convertToQuadraticConnectionPoints
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Preview
@Composable
private fun PreviewHourlyWindForecastWidget() {
    HourlyWindForecastWidget(
        graphSize = Size(40F, 100F),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyWindForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: Size,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val windSpeedFontSize = 40.sp

    Column(modifier = modifier) {
        Text(
            text = "Wind",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(Modifier.padding(bottom = 20.dp)) {
            Text(
                text = "${hourlyWeather.weatherForecast.first().wind.speed.value.toInt()}",
                fontSize = windSpeedFontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )

            Text(
                text = "kph",
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 4.dp, bottom = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .height(windSpeedFontSize.value.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color.Gray)
                    .width(2.dp)
            )

//            Text(
//                text = hourlyWeather.weatherForecast.first().wind.direction.toString(),
//                fontSize = 16.sp,
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//            )
        }

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.wind.speed.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().wind.speed.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].wind.speed.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().wind.speed.value
                    else hourlyWeather.weatherForecast[index - 1].wind.speed.value

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = hourlyWeather.maxWindSpeed.speed.value,
                    minValue = hourlyWeather.minWindSpeed.speed.value,
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