package com.application.weatherapp.model.weather.statistics

class Speed(
    val value: Float,
    val unit: SpeedUnit
)

enum class SpeedUnit {
    KILOMETERS_PER_HOUR,
    METERS_PER_HOUR
}
