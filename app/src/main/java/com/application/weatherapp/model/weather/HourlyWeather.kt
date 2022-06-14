package com.application.weatherapp.model.weather

import com.application.weatherapp.model.Temperature
import kotlin.streams.toList

data class HourlyWeather(
    var weatherForecast: MutableList<Weather> = mutableListOf()
) {
    val maxTemperature: Temperature get() =
        Temperature(weatherForecast.stream().map { it.currentTemperature.value }.toList().maxOrNull() ?: 0F, weatherForecast.first().currentTemperature.unit)

    val minTemperature: Temperature get() =
        Temperature(weatherForecast.stream().map { it.currentTemperature.value }.toList().minOrNull() ?: 0F, weatherForecast.first().currentTemperature.unit)

}