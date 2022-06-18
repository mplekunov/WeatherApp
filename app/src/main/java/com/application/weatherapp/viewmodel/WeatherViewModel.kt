package com.application.weatherapp.viewmodel

import androidx.lifecycle.*
import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.model.weather.Weather
import com.application.weatherapp.model.weather.WeatherType
import com.application.weatherapp.model.weather.statistics.*
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
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

    private fun downloadWeatherData() {
        // For Testing
        viewModelScope.launch {
            val hourlyWeather = SampleHourlyWeatherProvider().values.first()

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