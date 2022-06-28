package com.application.weatherapp.network.api.json

import com.squareup.moshi.Json

data class MetNorwayResponse(
    @Json(name = "properties") val weatherForecast: WeatherForecast
)

data class WeatherForecast(
    @Json(name = "timeseries") val hourlyForecast: List<Weather>,
    @Json(name = "meta") val metData: MetData
)

data class MetData(
    @Json(name = "units") val metUnits: MetUnit
)

data class MetUnit(
    @Json(name = "air_pressure_at_sea_level") val pressureUnit: String,
    @Json(name = "air_temperature") val temperatureUnit: String,
    @Json(name = "cloud_area_fraction") val cloudCoverUnit: String,
    @Json(name = "dew_point_temperature") val dewPointUnit: String,
    @Json(name = "fog_area_fraction") val fogUnit: String,
    @Json(name = "precipitation_amount") val precipitationUnit: String,
    @Json(name = "wind_from_direction") val windDirectionUnit: String,
    @Json(name = "wind_speed") val windSpeedUnit: String,
    @Json(name = "relative_humidity") val humidityUnit: String
)

data class Weather(
    @Json(name = "time") val time : String,
    @Json(name = "data") val forecast: Forecast
)

data class Forecast(
    @Json(name = "instant") val current: CurrentForecast,
    @Json(name = "next_1_hours") val nextHour: NextHourForecast?
)

data class NextHourForecast(
    @Json(name = "summary") val weatherType: WeatherType,
    @Json(name = "details") val precipitation: Precipitation
)

data class Precipitation(
    @Json(name = "precipitation_amount") val value: Float
)

data class WeatherType(
    @Json(name = "symbol_code") val type: String
)

data class CurrentForecast(
    @Json(name = "details") val details: Details
)

data class Details(
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