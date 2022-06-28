package com.application.weatherapp.model.weather.statistics

data class Pressure(
    val value: Float,
    val unit: PressureUnit,
)

enum class PressureUnit(val unit: String) {
    MILLI_BAR("mBar"),
    HECTOPASCAL("hPa"),
    NONE("N/A")
}