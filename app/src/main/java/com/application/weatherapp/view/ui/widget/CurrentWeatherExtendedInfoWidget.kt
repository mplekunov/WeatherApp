package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.weather.DailyWeather

@Composable
fun CurrentWeatherExtendedInfoWidget(
    modifier: Modifier,
    dailyWeather: DailyWeather?
) {
    Column(modifier = modifier) {
        Text(
            text = "Currently",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Cloud cover",
                fontSize = 16.sp
            )

            Text(
                text = String.format("%.1f", dailyWeather?.hourlyWeather?.weatherForecast?.first()?.cloudCover?.value) + "%",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Humidity",
                fontSize = 16.sp
            )

            Text(
                text = String.format("%.1f", dailyWeather?.hourlyWeather?.weatherForecast?.first()?.humidity?.value) + "%",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 57.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Pressure",
                fontSize = 16.sp
            )

            Text(
                text = String.format("%.2f mBar", dailyWeather?.hourlyWeather?.weatherForecast?.first()?.pressure?.value),
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 63.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Dew point",
                fontSize = 16.sp
            )

            Text(
                text = String.format("%.1f C", dailyWeather?.hourlyWeather?.weatherForecast?.first()?.dewPoint?.temperature?.value),
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 51.dp)
            )
        }
    }
}