package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.ui.theme.Typography
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider

@Composable
@Preview
private fun PreviewCurrentWeatherExtendedInfoWidget() {
//    CurrentWeatherExtendedInfoWidget(
//        modifier = Modifier,
//        dailyWeather = SampleHourlyWeatherProvider().
//    )
}

@Composable
fun CurrentWeatherExtendedInfoWidget(
    modifier: Modifier,
    dailyWeather: DailyWeather?
) {
    Column(modifier = modifier) {
        Text(
            text = "Currently",
            style = Typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Cloud cover",
                style = Typography.labelMedium
            )

            Text(
                text = String.format(
                    "%.1f",
                    dailyWeather?.hourlyWeather?.weatherForecast?.first()?.cloudCover?.value
                ) + " ${dailyWeather?.hourlyWeather?.weatherForecast?.first()?.cloudCover?.unit?.unit}",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 40.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Humidity",
                style = Typography.labelMedium
            )

            Text(
                text = String.format(
                    "%.1f",
                    dailyWeather?.hourlyWeather?.weatherForecast?.first()?.humidity?.value
                ) + " ${dailyWeather?.hourlyWeather?.weatherForecast?.first()?.humidity?.unit?.unit}",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 57.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Pressure",
                style = Typography.labelMedium
            )

            Text(
                text = String.format(
                    "%.2f",
                    dailyWeather?.hourlyWeather?.weatherForecast?.first()?.pressure?.value
                ) + " ${dailyWeather?.hourlyWeather?.weatherForecast?.first()?.pressure?.unit?.unit}",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 63.dp)
            )
        }

        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text(
                text = "Dew point",
                style = Typography.labelMedium
            )

            Text(
                text = String.format(
                    "%.1f",
                    dailyWeather?.hourlyWeather?.weatherForecast?.first()?.dewPoint?.temperature?.value
                ) + " ${dailyWeather?.hourlyWeather?.weatherForecast?.first()?.dewPoint?.temperature?.unit?.unit}",
                style = Typography.labelMedium,
                modifier = Modifier.padding(start = 51.dp)
            )
        }
    }
}