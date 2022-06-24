package com.application.weatherapp.network.api

import com.application.weatherapp.model.weather.HourlyWeather

interface WeatherApi {
    suspend fun getHourlyForecast(latitude: Double, longitude: Double) : HourlyWeather
}