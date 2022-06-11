package com.application.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.weatherapp.model.Weather
import com.application.weatherapp.model.WeatherType
import kotlinx.coroutines.launch
import java.time.LocalDate

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<Weather>()

    val currentWeather: LiveData<Weather> = _currentWeather

    init {
        downloadWeatherData()
    }

    private fun downloadWeatherData() {
        viewModelScope.launch {
            _currentWeather.value = Weather(LocalDate.now(), 10F, 20F, 15F, 18F, WeatherType.RAINY)
        }
    }
}