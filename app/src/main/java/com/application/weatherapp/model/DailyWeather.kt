package com.application.weatherapp.model

import java.time.LocalDate
import java.time.LocalDateTime

class DailyWeather(
    date: LocalDateTime,
    feelingTemperature: Float,
    currentTemperature: Float,
    weatherType: WeatherType
) : Weather(date, feelingTemperature, currentTemperature, weatherType) {

    var dayTemperature: Float = 0F
    var nightTemperature: Float = 0F

    var hourlyForecast: MutableList<Weather>? = null
}