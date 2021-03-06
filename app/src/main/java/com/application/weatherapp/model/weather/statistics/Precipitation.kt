package com.application.weatherapp.model.weather.statistics

data class Precipitation(
    val value: Float,
    val unit: PrecipitationUnit
)

enum class PrecipitationUnit(val unit: String) {
    MILLIMETER("mm"),
    NONE("N/A")
}