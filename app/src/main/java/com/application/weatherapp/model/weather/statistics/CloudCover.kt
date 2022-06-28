package com.application.weatherapp.model.weather.statistics

data class CloudCover(
    val value: Float,
    val unit: CloudCoverUnit
)

enum class CloudCoverUnit(val unit:String) {
    PERCENTAGE("%"),
    NONE("N/A"),
}