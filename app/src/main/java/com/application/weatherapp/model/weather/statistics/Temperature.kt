package com.application.weatherapp.model.weather.statistics

data class Temperature(
    val value: Float,
    val unit: TemperatureUnit
)

enum class TemperatureUnit(val unit: String) {
    CELSIUS("C"),
    FAHRENHEIT("F"),
    KELVIN("K"),
    NONE("N/A")
}