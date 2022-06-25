package com.application.weatherapp.model.weather

import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.statistics.*
import java.time.LocalDateTime

class DailyWeather(
    val hourlyWeather: HourlyWeather
) {
    val dayTemperature: Temperature = hourlyWeather.maxTemperature
    val nightTemperature: Temperature = hourlyWeather.minTemperature
}