package com.application.weatherapp.model.weather

import com.application.weatherapp.model.weather.statistics.Temperature

class DailyWeather(
    val hourlyWeather: HourlyWeather
) {
    val dayTemperature: Temperature = hourlyWeather.maxTemperature
    val nightTemperature: Temperature = hourlyWeather.minTemperature
}