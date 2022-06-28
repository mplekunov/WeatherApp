package com.application.weatherapp.network.api.service

import com.application.weatherapp.model.weather.statistics.Temperature
import com.application.weatherapp.model.weather.statistics.TemperatureUnit
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

        val temperatureUnit = convertTemperatureUnit(response.weatherForecast.metData.metUnits.temperatureUnit)
        val pressureUnit = convertPressureUnit(response.weatherForecast.metData.metUnits.pressureUnit)
        val humidityUnit = convertHumidityUnit(response.weatherForecast.metData.metUnits.humidityUnit)
        val cloudCoverUnit = convertCloudCoverUnit(response.weatherForecast.metData.metUnits.cloudCoverUnit)
        val dewPointUnit = convertTemperatureUnit(response.weatherForecast.metData.metUnits.dewPointUnit)
        val precipitationUnit = convertPrecipitationUnit(response.weatherForecast.metData.metUnits.precipitationUnit)
        val speedUnit = convertSpeedUnit(response.weatherForecast.metData.metUnits.windSpeedUnit)
        val windDirectionUnit = convertDirectionUnit(response.weatherForecast.metData.metUnits.windDirectionUnit)

        // First 24 hours from the current time
        for (i in 0 until 24) {
            val apiWeather = response.weatherForecast.hourlyForecast[i]

            val apiWeatherDetails = apiWeather.forecast.current.details
            val dateTime = TimeFormatter.formatZonedTime(ZonedDateTime.parse(apiWeather.time))

            val weather = Weather(
                currentTemperature = Temperature(
                    apiWeatherDetails.temperature,
                    temperatureUnit
                ),
                pressure = Pressure(
                    apiWeatherDetails.pressure,
                    pressureUnit
                ),
                humidity = Humidity(
                    apiWeatherDetails.humidity,
                    humidityUnit
                ),
                cloudCover = CloudCover(
                    apiWeatherDetails.cloudCover,
                    cloudCoverUnit
                ),
                dewPoint = DewPoint(
                    Temperature(
                        apiWeatherDetails.dewPoint,
                        dewPointUnit
                    )
                ),
                precipitation = Precipitation(
                    apiWeather.forecast.nextHour?.precipitation?.value ?: 0F,
                    precipitationUnit
                ),
                wind = Wind(
                    Speed(apiWeatherDetails.windSpeed, speedUnit),
                    Direction(apiWeatherDetails.windDirection, windDirectionUnit)
                ),
                feelingTemperature = Temperature(
                    apiWeatherDetails.temperature,
                    temperatureUnit
                ),
                date = dateTime,
                weatherType = convertWeatherType(apiWeather.forecast.nextHour?.weatherType?.type ?: "")
            )

            weatherForecast.add(weather)
        }

        return HourlyWeather(weatherForecast)
    }

    private fun convertTemperatureUnit(temperatureUnit: String): TemperatureUnit {
        return when(temperatureUnit) {
            "celsius" -> TemperatureUnit.CELSIUS
            "kelvin" -> TemperatureUnit.KELVIN
            "fahrenheit" -> TemperatureUnit.FAHRENHEIT
            else -> TemperatureUnit.NONE
        }
    }

    private fun convertPressureUnit(pressureUnit: String): PressureUnit {
        return when(pressureUnit) {
            "hPa" -> PressureUnit.HECTOPASCAL
            else -> PressureUnit.NONE
        }
    }

    private fun convertHumidityUnit(humidityUnit: String): HumidityUnit {
        return when(humidityUnit) {
            "%" -> HumidityUnit.PERCENTAGE
            else -> HumidityUnit.NONE
        }
    }

    private fun convertCloudCoverUnit(cloudCoverUnit: String): CloudCoverUnit {
        return when(cloudCoverUnit) {
            "%" -> CloudCoverUnit.PERCENTAGE
            else -> CloudCoverUnit.NONE
        }
    }

    private fun convertPrecipitationUnit(precipitationUnit: String): PrecipitationUnit {
        return when(precipitationUnit) {
            "mm" -> PrecipitationUnit.MILLIMETER
            else -> PrecipitationUnit.NONE
        }
    }

    private fun convertSpeedUnit(speedUnit: String): SpeedUnit {
        return when(speedUnit) {
            "m/s" -> SpeedUnit.METERS_PER_SECOND
            else -> SpeedUnit.NONE
        }
    }

    private fun convertDirectionUnit(directionUnit: String): DirectionUnit {
        return when(directionUnit) {
            "degrees" -> DirectionUnit.DEGREES
            else -> DirectionUnit.NONE
        }
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