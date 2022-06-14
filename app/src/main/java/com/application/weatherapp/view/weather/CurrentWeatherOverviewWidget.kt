package com.application.weatherapp.view.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.weatherapp.R
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.model.weather.WeatherType
import com.application.weatherapp.viewmodel.DateTimeViewModel

@Composable
fun CurrentWeatherWidget(
    currentWeather: DailyWeather,
    modifier: Modifier,
    viewModel: DateTimeViewModel = viewModel()
) {
    val currentDate = viewModel.date.observeAsState()
    val currentTime = viewModel.time.observeAsState()

    Column {
        DateTimeOverview(
            date = currentDate.value ?: "June 5, 2022",
            time = currentTime.value ?: "10:55",
            modifier = modifier
        )

        CurrentWeatherOverview(
            currentWeather = currentWeather,
            modifier = modifier
        )
    }
}

@Composable
private fun DateTimeOverview(
    date: String,
    time: String,
    modifier: Modifier
) {
    Box(
        modifier
            .fillMaxWidth()
    ) {
        Text(
            text = date
        )

        Text(
            text = time,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun CurrentWeatherOverview(
    currentWeather: DailyWeather,
    modifier: Modifier
) {
    // Day/Night Temperatures
    Text(
        text = "Day ${currentWeather.dayTemperature.value.toInt()} Night ${currentWeather.nightTemperature.value.toInt()}",
        modifier = modifier
    )

    // Current Weather Info
    Box(
        modifier
            .fillMaxWidth()
            .padding(end = 20.dp)
    ) {
        // First Column is current/feeling temperature
        Column(
            modifier
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "${currentWeather.currentTemperature.value.toInt()}",
                fontSize = 86.sp,
            )

            Text(
                text = "Feels like ${currentWeather.feelingTemperature.value.toInt()}",
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Second Column is Weather Forecast (Icon/Description)
        Column(
            modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = getWeatherIconPainter(weatherType = currentWeather.weatherType),
                contentDescription = currentWeather.weatherDescription,
                modifier = Modifier
                    .size(100.dp)
            )

            Text(
                text = currentWeather.weatherDescription,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}