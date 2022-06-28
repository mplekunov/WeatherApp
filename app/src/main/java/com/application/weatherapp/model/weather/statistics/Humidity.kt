package com.application.weatherapp.model.weather.statistics

data class Humidity(
    val value: Float,
    val unit: HumidityUnit
)

enum class HumidityUnit(val unit: String) {
    PERCENTAGE("%"),
    NONE("N/A")
}