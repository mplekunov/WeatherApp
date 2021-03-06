package com.application.weatherapp.model.weather

import com.application.weatherapp.model.weather.statistics.Temperature
import com.application.weatherapp.model.weather.statistics.*
import java.time.LocalDateTime


open class Weather(
    val date: LocalDateTime,
    val feelingTemperature: Temperature,
    val currentTemperature: Temperature,
    val weatherType: WeatherType,
    var cloudCover: CloudCover,
    var humidity: Humidity,
    var pressure: Pressure,
    var dewPoint: DewPoint,
    var precipitation: Precipitation,
    var wind: Wind,
    var ultraVioletIndex: Float
    ) {
    val weatherDescription get() = when(weatherType) {
        WeatherType.WINDY -> "Windy"
        WeatherType.RAINY -> "Rainy"
        WeatherType.SUNNY -> "Sunny"
        WeatherType.CLOUDY -> "Cloudy"
        WeatherType.SNOWY -> "Snowy"
        WeatherType.SNOWY_RAINY -> "Snow with Rain"
        WeatherType.PARTLY_RAINY -> "Partly Rainy"
        WeatherType.PARTLY_THUNDERSTORM -> "Partly Thunderstorm"
        WeatherType.PARTLY_CLOUDY_DAY -> "Partly Cloudy"
        WeatherType.PARTLY_CLOUDY_NIGHT -> "Partly Cloudy"
        WeatherType.PARTLY_SNOWY -> "Partly Snowy"
        WeatherType.PARTLY_SNOWY_RAINY -> "Partly Snow with Rain"
        WeatherType.HEAVY_RAIN -> "Heavy Rain"
        WeatherType.HAZY -> "Hazy"
        WeatherType.FOGGY -> "Foggy"
        WeatherType.HEAVY_SNOW -> "Heavy Snow"
        WeatherType.THUNDERSTORM -> "Thunderstorm"
        WeatherType.HURRICANE -> "Hurricane"
        WeatherType.TORNADO -> "Tornado"
        else -> "API Error"
    }
}