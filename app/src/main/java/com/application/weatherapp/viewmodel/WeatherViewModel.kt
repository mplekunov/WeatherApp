package com.application.weatherapp.viewmodel

import androidx.lifecycle.*
import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.model.weather.Weather
import com.application.weatherapp.model.weather.WeatherType
import com.application.weatherapp.model.weather.statistics.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.random.Random

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<DailyWeather>()
    val currentWeather: LiveData<DailyWeather> = _currentWeather

    private val _weeklyWeather = MutableLiveData<MutableList<DailyWeather>>()
    val weeklyWeather: LiveData<MutableList<DailyWeather>> = _weeklyWeather

    init {
        downloadWeatherData()
    }

    private val precipitationRand get() = Random.nextDouble(0.0, 5.0)

    private fun downloadWeatherData() {
        // For Testing
        viewModelScope.launch {
            val hourlyWeather = HourlyWeather(mutableListOf(
                Weather(
                    LocalDateTime.now(),
                    Temperature(0F, TemperatureUnit.CELSIUS),
                    Temperature(0F, TemperatureUnit.CELSIUS),
                    WeatherType.CLOUDY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(0F, PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(1),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SUNNY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(2),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(3),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    WeatherType.THUNDERSTORM,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(4),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.PARTLY_CLOUDY_DAY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(5),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(6),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    WeatherType.TORNADO,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(7),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(8),
                    Temperature(30F, TemperatureUnit.CELSIUS),
                    Temperature(30F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(9),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(10),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(11),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(12),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(13),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(14),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(15),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(16),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(17),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(18),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(19),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(20),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(21),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(22),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                ),
                Weather(
                    LocalDateTime.now().plusHours(23),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY,
                    CloudCover(precipitationRand.toFloat(), CloudCoverUnit.PERCENTAGE),
                    Humidity(precipitationRand.toFloat(), HumidityUnit.PERCENTAGE),
                    Pressure(precipitationRand.toFloat(), PressureUnit.MILLI_BAR),
                    DewPoint(Temperature(precipitationRand.toFloat(), TemperatureUnit.CELSIUS)),
                    Precipitation(precipitationRand.toFloat(), PrecipitationUnit.MILLIMETER)
                )))


            _currentWeather.value = DailyWeather(hourlyWeather)
            _currentWeather.value!!.dayTemperature = Temperature(15F, TemperatureUnit.CELSIUS)
            _currentWeather.value!!.nightTemperature = Temperature(2F, TemperatureUnit.CELSIUS)

            _weeklyWeather.value = mutableListOf(
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather),
                DailyWeather(hourlyWeather)
            )
        }
    }
}