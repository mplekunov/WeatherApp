package com.application.weatherapp.model.weather.statistics

data class Pressure(
    val value: Float,
    val unit: PressureUnit,
)

enum class PressureUnit {
    MILLI_BAR
}
