package com.application.weatherapp.model.weather

import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.statistics.*
import kotlin.streams.toList

data class HourlyWeather(
    var weatherForecast: MutableList<Weather> = mutableListOf()
) {
    private val temperatureCollection get() = weatherForecast.stream().map { it.currentTemperature.value }.toList()
    private val humidityCollection get() = weatherForecast.stream().map { it.humidity.value }.toList()
    private val cloudCoverCollection get() = weatherForecast.stream().map { it.cloudCover.value }.toList()
    private val pressureCollection get() = weatherForecast.stream().map { it.pressure.value }.toList()
    private val dewPointCollection get() = weatherForecast.stream().map { it.dewPoint.temperature.value }.toList()
    private val precipitationCollection get() = weatherForecast.stream().map { it.precipitation.value }.toList()
    private val windCollection get() = weatherForecast.stream().map { it.wind.speed.value }.toList()

    val maxTemperature get() =
        Temperature(temperatureCollection.maxOrNull() ?: 0F, weatherForecast.first().currentTemperature.unit)

    val minTemperature get() =
        Temperature(temperatureCollection.minOrNull() ?: 0F, weatherForecast.first().currentTemperature.unit)

    val maxHumidity get() =
        Humidity(humidityCollection.maxOrNull() ?: 0F, weatherForecast.first().humidity.unit)

    val minHumidity get() =
        Humidity(humidityCollection.minOrNull() ?: 0F, weatherForecast.first().humidity.unit)

    val maxCloudCover get() =
        CloudCover(cloudCoverCollection.maxOrNull() ?: 0F, weatherForecast.first().cloudCover.unit)

    val minCloudCover get() =
        CloudCover(cloudCoverCollection.minOrNull() ?: 0F, weatherForecast.first().cloudCover.unit)

    val maxPressure get() =
        Pressure(pressureCollection.maxOrNull() ?: 0F, weatherForecast.first().pressure.unit)

    val minPressure get() =
        Pressure(pressureCollection.minOrNull() ?: 0F, weatherForecast.first().pressure.unit)

    val maxDewPoint get() =
        DewPoint(Temperature(dewPointCollection.maxOrNull() ?: 0F, weatherForecast.first().dewPoint.temperature.unit))

    val minDewPoint get() =
        DewPoint(Temperature(dewPointCollection.minOrNull() ?: 0F, weatherForecast.first().dewPoint.temperature.unit))

    val maxPrecipitation get() =
        Precipitation(precipitationCollection.maxOrNull() ?: 0F, weatherForecast.first().precipitation.unit)

    val minPrecipitation get() =
        Precipitation(precipitationCollection.minOrNull() ?: 0F, weatherForecast.first().precipitation.unit)

    val maxWindSpeed get() =
        Wind(Speed(windCollection.maxOrNull() ?: 0F, weatherForecast.first().wind.speed.unit),
            Direction(Float.MAX_VALUE, DirectionUnit.DEGREES)
        )

    val minWindSpeed get() =
        Wind(Speed(windCollection.minOrNull() ?: 0F, weatherForecast.first().wind.speed.unit),
            Direction(Float.MAX_VALUE, DirectionUnit.DEGREES)
        )
}