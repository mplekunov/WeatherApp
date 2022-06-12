package com.application.weatherapp.viewmodel

import androidx.lifecycle.*
import com.application.weatherapp.model.DailyWeather
import com.application.weatherapp.model.WeatherType
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
            val localDateTime = LocalDateTime.now()
            _currentWeather.value = DailyWeather(localDateTime, 10F, 20F, WeatherType.RAINY)
            _currentWeather.value!!.dayTemperature = 15F
            _currentWeather.value!!.nightTemperature = 2F

            _weeklyWeather.value = mutableListOf(
                DailyWeather(localDateTime, 10F, 20F, WeatherType.SUNNY),
                DailyWeather(localDateTime.plusDays(1), 10F, 20F, WeatherType.THUNDERSTORM),
                DailyWeather(localDateTime.plusDays(2), 10F, 20F, WeatherType.PARTLY_RAINY),
                DailyWeather(localDateTime.plusDays(3), 10F, 20F, WeatherType.PARTLY_SNOWY_RAINY),
                DailyWeather(localDateTime.plusDays(4), 10F, 20F, WeatherType.SNOWY),
                DailyWeather(localDateTime.plusDays(5), 10F, 20F, WeatherType.CLOUDY),
                DailyWeather(localDateTime.plusDays(6), 10F, 20F, WeatherType.PARTLY_CLOUDY_DAY)
            )
        }
    }
}