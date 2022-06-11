package com.application.weatherapp

import java.time.LocalDate


class Weather(
    val date: LocalDate,
    val dayTemperature: Float,
    val nightTemperature: Float,
    val feelingTemperature: Float,
    val currentTemperature: Float,
    val weatherType: WeatherType
    ) {
}