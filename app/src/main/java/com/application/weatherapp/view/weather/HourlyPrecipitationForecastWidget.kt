package com.application.weatherapp.view.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.weather.HourlyWeather

@Composable
fun HourlyPrecipitationForecastWidget(
    modifier: Modifier = Modifier,
    hourlyWeather: HourlyWeather
) {
    Column(modifier = modifier) {
        Text(
            text = "Precipitation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        LazyRow(modifier = Modifier) {
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

//                    HourlyPrecipitationCurve(
//                        prevValue = prevValue,
//                        currentValue = currentValue,
//                        nextValue = nextValue,
//                        maxValue = hourlyWeather.maxTemperature.value,
//                        minValue = hourlyWeather.minTemperature.value,
//                        canvasSize = Size(60F, 150F),
//                    )
            }
        }
    }
}