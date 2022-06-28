package com.application.weatherapp.viewmodel.sample

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.application.weatherapp.model.weather.statistics.Temperature
import com.application.weatherapp.model.weather.statistics.TemperatureUnit
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.model.weather.Weather
import com.application.weatherapp.model.weather.WeatherType
import com.application.weatherapp.model.weather.statistics.*
import java.time.LocalDateTime
import kotlin.random.Random

class SampleHourlyWeatherProvider : PreviewParameterProvider<HourlyWeather> {
    override val values: Sequence<HourlyWeather> = sequenceOf(HourlyWeather(mutableListOf()))

    private val realTemperatureRand get() = Random.nextDouble(0.0, 20.0).toFloat()
    private val feelingTemperatureRand get() = Random.nextDouble(10.0, 40.0).toFloat()
    private val cloudCoverRand get() = Random.nextDouble(0.0, 100.0).toFloat()
    private val humidityRand get() = Random.nextDouble(0.0, 100.0).toFloat()
    private val pressureRand get() = Random.nextDouble(900.0, 1300.0).toFloat()
    private val dewPointRand get() = Random.nextDouble(5.0, 30.0).toFloat()
    private val precipitationRand get() = Random.nextDouble(0.0, 5.0).toFloat()
    private val windRand get() = Random.nextDouble(0.0, 20.0).toFloat()
    private val weatherType
        get() = WeatherType.values()[Random.nextInt(
            0,
            WeatherType.values().lastIndex
        )]
//    private val direction
//        get() = Direction.values()[Random.nextInt(
//            0,
//            Direction.Directions.values().lastIndex
//        )]

    init {
        val hourlyWeather = HourlyWeather(mutableListOf())
        val time = LocalDateTime.now()
        for (i in 0 until 24) {
            hourlyWeather.weatherForecast.add(
                Weather(
                    date = time.plusHours(i.toLong()),
                    cloudCover = CloudCover(cloudCoverRand, CloudCoverUnit.PERCENTAGE),
                    currentTemperature = Temperature(
                        realTemperatureRand,
                        TemperatureUnit.CELSIUS
                    ),
                    feelingTemperature = Temperature(
                        feelingTemperatureRand,
                        TemperatureUnit.CELSIUS
                    ),
                    dewPoint = DewPoint(Temperature(dewPointRand, TemperatureUnit.CELSIUS)),
                    humidity = Humidity(humidityRand, HumidityUnit.PERCENTAGE),
                    precipitation = Precipitation(
                        precipitationRand,
                        PrecipitationUnit.MILLIMETER
                    ),
                    pressure = Pressure(pressureRand, PressureUnit.MILLI_BAR),
                    wind = Wind(
                        Speed(windRand, SpeedUnit.KILOMETERS_PER_HOUR),
                        Direction(Float.MAX_VALUE, DirectionUnit.DEGREES)
                    ),
                    weatherType = weatherType
                )
            )
        }

        values.first().weatherForecast.addAll(hourlyWeather.weatherForecast)
    }
}