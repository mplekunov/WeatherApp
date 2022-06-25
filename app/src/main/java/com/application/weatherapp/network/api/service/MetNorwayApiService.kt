package com.application.weatherapp.network.api.service

import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.formatter.TimeFormatter
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.model.weather.Weather
import com.application.weatherapp.model.weather.WeatherType
import com.application.weatherapp.model.weather.statistics.*
import com.application.weatherapp.network.NetworkInterceptor
import com.application.weatherapp.network.api.WeatherApi
import com.application.weatherapp.network.api.json.MetNorwayResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.ZonedDateTime

private val BASE_URL = "https://api.met.no/weatherapi/locationforecast/2.0/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(OkHttpClient.Builder()
            .addNetworkInterceptor(NetworkInterceptor())
            .build())
    .baseUrl(BASE_URL)
    .build()

interface MetNorwayApiService {
    @GET("complete?")
    suspend fun getForecast(@Query("lat") latitude: Double, @Query("lon") longitude: Double): MetNorwayResponse
}

object MetNorwayApi : WeatherApi {
    override suspend fun getHourlyForecast(latitude: Double, longitude: Double): HourlyWeather {
        val response = retrofit.create(MetNorwayApiService::class.java).getForecast(latitude, longitude)

        val weatherForecast = mutableListOf<Weather>()

        // First 24 hours from the current time
        for (i in 0 until 24) {
            val apiWeather = response.weatherForecast.hourlyForecast[i]

            val apiWeatherDetails = apiWeather.forecast.current.details
            val dateTime = TimeFormatter.formatZonedTime(ZonedDateTime.parse(apiWeather.time))

            val weather = Weather(
                currentTemperature = Temperature(apiWeatherDetails.temperature, TemperatureUnit.CELSIUS),
                pressure = Pressure(apiWeatherDetails.pressure, PressureUnit.HECTOPASCAL),
                humidity = Humidity(apiWeatherDetails.humidity, HumidityUnit.PERCENTAGE),
                cloudCover = CloudCover(apiWeatherDetails.cloudCover, CloudCoverUnit.PERCENTAGE),
                dewPoint = DewPoint(Temperature(apiWeatherDetails.dewPoint, TemperatureUnit.CELSIUS)),
                precipitation = Precipitation(apiWeather.forecast.nextHour?.precipitation?.value ?: 0F, PrecipitationUnit.MILLIMETER),
                wind = Wind(
                    Speed(apiWeatherDetails.windSpeed, SpeedUnit.METERS_PER_SECOND),
                    Direction(apiWeatherDetails.windDirection, DirectionUnit.DEGREES)
                ),
                feelingTemperature = Temperature(apiWeatherDetails.temperature, TemperatureUnit.CELSIUS),
                date = dateTime,
                weatherType = convertWeatherType(apiWeather.forecast.nextHour?.weatherType?.type ?: "")
            )

            weatherForecast.add(weather)
        }

        return HourlyWeather(weatherForecast)
    }

    private fun convertWeatherType(weatherType: String): WeatherType {
        return when(weatherType.split("_").first()) {
            "clearsky" -> WeatherType.SUNNY
            "cloudy" -> WeatherType.CLOUDY
            "fair" -> WeatherType.PARTLY_CLOUDY_DAY
            "fog" -> WeatherType.FOGGY
            "heavyrain" -> WeatherType.HEAVY_RAIN
            "heavyrainandthunder" -> WeatherType.THUNDERSTORM
            "heavyrainshowers" -> WeatherType.HEAVY_RAIN
            "heavyrainshowersandthunder" -> WeatherType.THUNDERSTORM
            "heavysleet" -> WeatherType.SNOWY_RAINY
            "heavysleetandthunder" -> WeatherType.SNOWY_RAINY
            "heavysleetshowers" -> WeatherType.SNOWY_RAINY
            "heavysleetshowersandthunder" -> WeatherType.SNOWY_RAINY
            "heavysnow" -> WeatherType.HEAVY_SNOW
            "heavysnowandthunder" -> WeatherType.HEAVY_SNOW
            "heavysnowshowers" -> WeatherType.HEAVY_SNOW
            "heavysnowshowersandthunder" -> WeatherType.HEAVY_SNOW
            "lightrain" -> WeatherType.PARTLY_RAINY
            "lightrainandthunder" -> WeatherType.PARTLY_THUNDERSTORM
            "lightrainshowers" -> WeatherType.PARTLY_RAINY
            "lightrainshowersandthunder" -> WeatherType.PARTLY_THUNDERSTORM
            "lightsleet" -> WeatherType.SNOWY_RAINY
            "lightsleetandthunder" -> WeatherType.SNOWY_RAINY
            "lightsleetshowers" -> WeatherType.SNOWY_RAINY
            "lightsnow" -> WeatherType.SNOWY
            "lightsnowandthunder" -> WeatherType.SNOWY
            "lightsnowshowers" -> WeatherType.SNOWY
            "lightssleetshowersandthunder" -> WeatherType.SNOWY
            "lightssnowshowersandthunder" -> WeatherType.SNOWY
            "partlycloudy" -> WeatherType.PARTLY_CLOUDY_DAY
            "rain" -> WeatherType.RAINY
            "rainandthunder" -> WeatherType.THUNDERSTORM
            "rainshowers" -> WeatherType.RAINY
            "rainshowersandthunder" -> WeatherType.THUNDERSTORM
            "sleet" -> WeatherType.SNOWY_RAINY
            "sleetandthunder" -> WeatherType.SNOWY_RAINY
            "sleetshowers" -> WeatherType.SNOWY_RAINY
            "sleetshowersandthunder" -> WeatherType.SNOWY_RAINY
            "snow" -> WeatherType.SNOWY
            "snowandthunder" -> WeatherType.SNOWY
            "snowshowers" -> WeatherType.SNOWY
            "snowshowersandthunder" -> WeatherType.SNOWY
            else -> WeatherType.NONE
        }
    }
}