package com.application.weatherapp.network

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "properties") val weatherForecast: ApiWeatherForecast
)

data class ApiWeatherForecast(
    @Json(name = "timeseries") val hourlyForecast: List<ApiWeather>
)

data class ApiWeather(
    @Json(name = "time") val time : String,
    @Json(name = "data") val weatherData: ApiWeatherData
)

data class ApiWeatherData(
    @Json(name = "instant") val current: ApiCurrent,
    @Json(name = "next_1_hours") val nextHour: ApiHourlyData?
)

data class ApiHourlyData(
    @Json(name = "summary") val weatherType: ApiWeatherType,
    @Json(name = "details") val precipitation: ApiPrecipitation
)

data class ApiPrecipitation(
    @Json(name = "precipitation_amount") val value: Float
)

data class ApiWeatherType(
    @Json(name = "symbol_code") val type: String
)

data class ApiCurrent(
    @Json(name = "details") val details: ApiDetails
)

data class ApiDetails(
    @Json(name = "air_pressure_at_sea_level")
    val pressure: Float,

    @Json(name = "air_temperature")
    val temperature: Float,

    @Json(name = "cloud_area_fraction")
    val cloudCover: Float,

    @Json(name = "relative_humidity")
    val humidity: Float,

    @Json(name = "wind_from_direction")
    val windDirection: Float,

    @Json(name = "wind_speed")
    val windSpeed: Float,

    @Json(name = "dew_point_temperature")
    val dewPoint: Float,

    @Json(name = "ultraviolet_index_clear_sky")
    val ultravioletIndex: Float?
)