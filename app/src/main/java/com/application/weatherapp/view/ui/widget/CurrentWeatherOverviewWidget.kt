package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.ui.theme.Typography
import com.application.weatherapp.view.ui.icon.getWeatherIconPainter
import com.application.weatherapp.viewmodel.DateTimeViewModel

@Composable
fun CurrentWeatherOverviewWidget(
    dailyWeather: DailyWeather,
    modifier: Modifier,
    dateTimeViewModel: DateTimeViewModel = viewModel()
) {
    val currentDate = dateTimeViewModel.date.observeAsState()
    val currentTime = dateTimeViewModel.time.observeAsState()

    Column(modifier = modifier) {
        DateTimeOverview(
            date = currentDate.value ?: "June 5, 2022",
            time = currentTime.value ?: "10:55",
            modifier = Modifier.fillMaxWidth()
        )

        // Day/Night Temperatures
        Text(
            text = "Day ${dailyWeather.dayTemperature.value.toInt()} Night ${dailyWeather.nightTemperature.value.toInt()}",
            style = Typography.labelMedium,
            modifier = Modifier
        )

        CurrentWeatherOverview(
            dailyWeather = dailyWeather,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()

        )
    }
}

@Composable
private fun DateTimeOverview(
    date: String,
    time: String,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = date,
            style = Typography.labelMedium
        )

        Text(
            text = time,
            style = Typography.labelMedium,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun CurrentWeatherOverview(
    dailyWeather: DailyWeather,
    modifier: Modifier
) {
    // Current Weather Info
    Box(modifier = modifier) {
        // First Column is current/feeling temperature
        Column(
            Modifier
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "${dailyWeather.hourlyWeather.weatherForecast.first().currentTemperature.value.toInt()}",
                style = Typography.titleLarge
            )

            Text(
                text = "Feels like ${dailyWeather.hourlyWeather.weatherForecast.first().feelingTemperature.value.toInt()}",
                style = Typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Second Column is Weather Forecast (Icon/Description)
        Column(
            Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = getWeatherIconPainter(weatherType = dailyWeather.hourlyWeather.weatherForecast.first().weatherType),
                contentDescription = dailyWeather.hourlyWeather.weatherForecast.first().weatherDescription,
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = dailyWeather.hourlyWeather.weatherForecast.first().weatherDescription,
                style = Typography.labelMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}