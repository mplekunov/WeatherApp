package com.application.weatherapp.model

data class Temperature(
    val value: Float,
    val unit: TemperatureUnit
)

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT,
    KELVIN
}