package com.application.weatherapp.view.weather

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
fun AdditionalWeatherInfoWidget(
    modifier: Modifier,
    currentWeather: DailyWeather?
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
                text = "${currentWeather?.cloudCover?.value} %",
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
                text = "${currentWeather?.humidity?.value} %",
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
                text = "${currentWeather?.pressure?.value} mBar",
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
                text = "${currentWeather?.dewPoint?.temperature?.value} C",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 51.dp)
            )
        }
    }
}