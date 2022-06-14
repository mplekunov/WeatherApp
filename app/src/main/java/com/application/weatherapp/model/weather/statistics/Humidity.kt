package com.application.weatherapp.model.weather.statistics

data class Humidity(
    val value: Float,
    val unit: HumidityUnit
)

enum class HumidityUnit {
    PERCENTAGE
}
