package com.application.weatherapp.model.weather.statistics

class Speed(
    val value: Float,
    val unit: SpeedUnit
)

enum class SpeedUnit(val unit: String) {
    KILOMETERS_PER_HOUR("km/h"),
    METERS_PER_HOUR("m/h"),
    METERS_PER_SECOND("m/s"),
    NONE("N/A")
}