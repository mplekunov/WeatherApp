package com.application.weatherapp.view.ui.weather

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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Preview
@Composable
fun HourlyWindForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather = SampleHourlyWeatherProvider().values.first()
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val windSpeedFontSize = 40.sp
    val canvasSize = Size(40F, 100F)

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
                    .padding(bottom = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .height(windSpeedFontSize.value.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color.Gray)
                    .width(2.dp)
            )

            Text(
                text = hourlyWeather.weatherForecast.first().wind.direction.toString(),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }

        LazyRow(
            modifier = Modifier.heightIn(max = 120.dp)
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


                Column(modifier = Modifier.fillMaxHeight()) {
                    HourlyPrecipitationGraph(
                        prevValue = prevValue,
                        currentValue = currentValue,
                        nextValue = nextValue,
                        maxValue = 20F,
                        minValue = hourlyWeather.minWindSpeed.speed.value,
                        canvasSize = canvasSize
                    )

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