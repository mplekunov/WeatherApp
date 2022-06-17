package com.application.weatherapp.view.ui.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.application.weatherapp.R
import com.application.weatherapp.model.weather.WeatherType

@Composable
fun getWeatherIconPainter(weatherType: WeatherType): Painter {
    return when (weatherType) {
        WeatherType.WINDY -> painterResource(R.drawable.ic_windy)
        WeatherType.TORNADO -> painterResource(R.drawable.ic_tornado)
        WeatherType.HURRICANE -> painterResource(R.drawable.ic_hurricane)

        WeatherType.CLOUDY -> painterResource(R.drawable.ic_cloudy)
        WeatherType.PARTLY_CLOUDY_DAY -> painterResource(R.drawable.ic_day_partly_cloudy)
        WeatherType.PARTLY_CLOUDY_NIGHT -> painterResource(R.drawable.ic_night_partly_cloudy)
        WeatherType.FOGGY -> painterResource(R.drawable.ic_foggy)

        WeatherType.SUNNY -> painterResource(R.drawable.ic_sunny)
        WeatherType.HAZY -> painterResource(R.drawable.ic_hazy)

        WeatherType.SNOWY -> painterResource(R.drawable.ic_snowy)
        WeatherType.SNOWY_RAINY -> painterResource(R.drawable.ic_snowy_rainy)
        WeatherType.PARTLY_SNOWY -> painterResource(R.drawable.ic_partly_snowy)
        WeatherType.PARTLY_SNOWY_RAINY -> painterResource(R.drawable.ic_partly_snowy_rainy)
        WeatherType.HEAVY_SNOW -> painterResource(R.drawable.ic_heavy_snow)

        WeatherType.RAINY -> painterResource(R.drawable.ic_rainy)
        WeatherType.PARTLY_RAINY -> painterResource(R.drawable.ic_partly_rainy)
        WeatherType.HEAVY_RAIN -> painterResource(R.drawable.ic_heavy_rain)
        WeatherType.THUNDERSTORM -> painterResource(R.drawable.ic_thunderstorm)
        WeatherType.PARTLY_THUNDERSTORM -> painterResource(R.drawable.ic_partly_thunderstorm)
    }
}