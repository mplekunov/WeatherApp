package com.application.weatherapp.viewmodel

import androidx.lifecycle.*
import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.model.weather.Weather
import com.application.weatherapp.model.weather.WeatherType
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<DailyWeather>()
    val currentWeather: LiveData<DailyWeather> = _currentWeather

    private val _weeklyWeather = MutableLiveData<MutableList<DailyWeather>>()
    val weeklyWeather: LiveData<MutableList<DailyWeather>> = _weeklyWeather

    init {
        downloadWeatherData()
    }

    private fun downloadWeatherData() {
        // For Testing
        viewModelScope.launch {
            val hourlyWeather = HourlyWeather(mutableListOf(
                Weather(
                    LocalDateTime.now(),
                    Temperature(0F, TemperatureUnit.CELSIUS),
                    Temperature(0F, TemperatureUnit.CELSIUS),
                    WeatherType.CLOUDY
                ),
                Weather(
                    LocalDateTime.now().plusHours(1),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SUNNY
                ),
                Weather(
                    LocalDateTime.now().plusHours(2),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(3),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    WeatherType.THUNDERSTORM
                ),
                Weather(
                    LocalDateTime.now().plusHours(4),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.PARTLY_CLOUDY_DAY
                ),
                Weather(
                    LocalDateTime.now().plusHours(5),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(6),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    WeatherType.TORNADO
                ),
                Weather(
                    LocalDateTime.now().plusHours(7),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(8),
                    Temperature(30F, TemperatureUnit.CELSIUS),
                    Temperature(30F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(9),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(10),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(11),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(12),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    Temperature(28F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(13),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    Temperature(27F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(14),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(15),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    Temperature(26F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(16),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    Temperature(25F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(17),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(18),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(19),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    Temperature(24F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(20),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(21),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(22),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(23F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                ),
                Weather(
                    LocalDateTime.now().plusHours(23),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    Temperature(29F, TemperatureUnit.CELSIUS),
                    WeatherType.SNOWY
                )))


            val localDateTime = LocalDateTime.now()
            _currentWeather.value = DailyWeather(localDateTime, Temperature(10F, TemperatureUnit.CELSIUS), Temperature(20F, TemperatureUnit.CELSIUS), WeatherType.RAINY, hourlyWeather)
            _currentWeather.value!!.dayTemperature = Temperature(15F, TemperatureUnit.CELSIUS)
            _currentWeather.value!!.nightTemperature = Temperature(2F, TemperatureUnit.CELSIUS)

            _weeklyWeather.value = mutableListOf(
                DailyWeather(localDateTime, Temperature(10F, TemperatureUnit.CELSIUS), Temperature(10F, TemperatureUnit.CELSIUS), WeatherType.SUNNY, hourlyWeather),
                DailyWeather(localDateTime.plusDays(1), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.THUNDERSTORM, hourlyWeather),
                DailyWeather(localDateTime.plusDays(2), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.PARTLY_RAINY, hourlyWeather),
                DailyWeather(localDateTime.plusDays(3), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.PARTLY_SNOWY_RAINY, hourlyWeather),
                DailyWeather(localDateTime.plusDays(4), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.SNOWY, hourlyWeather),
                DailyWeather(localDateTime.plusDays(5), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.CLOUDY, hourlyWeather),
                DailyWeather(localDateTime.plusDays(6), Temperature(15F, TemperatureUnit.CELSIUS), Temperature(15F, TemperatureUnit.CELSIUS), WeatherType.PARTLY_CLOUDY_DAY, hourlyWeather)
            )
        }
    }
}