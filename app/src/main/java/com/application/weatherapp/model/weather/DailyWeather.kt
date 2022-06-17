package com.application.weatherapp.model.weather

import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.statistics.*
import java.time.LocalDateTime

class DailyWeather(
    val hourlyWeather: HourlyWeather
) {
    val averageTemperature get() =
        (hourlyWeather.maxTemperature.value + hourlyWeather.minTemperature.value) / 2

    val averageHumidity get() =
        (hourlyWeather.maxHumidity.value + hourlyWeather.minHumidity.value) / 2

    val averagePressure get() =
        (hourlyWeather.maxPressure.value + hourlyWeather.minPressure.value) / 2

    val averageDewPoint get() =
        (hourlyWeather.maxDewPoint.temperature.value + hourlyWeather.minDewPoint.temperature.value) / 2

    val averageCloudCover get() =
        (hourlyWeather.maxCloudCover.value + hourlyWeather.minCloudCover.value) / 2

    val averagePrecipitation get() =
        (hourlyWeather.maxPrecipitation.value + hourlyWeather.minPrecipitation.value) / 2

    var dayTemperature: Temperature = Temperature(0F, TemperatureUnit.CELSIUS)
    var nightTemperature: Temperature = Temperature(0F, TemperatureUnit.CELSIUS)
}