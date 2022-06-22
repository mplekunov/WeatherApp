package com.application.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.network.api.WeatherApi
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<DailyWeather>()
    val currentWeather: LiveData<DailyWeather> = _currentWeather

    private val _weeklyWeather = MutableLiveData<MutableList<DailyWeather>>()
    val weeklyWeather: LiveData<MutableList<DailyWeather>> = _weeklyWeather

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    init {
        downloadWeatherData()
    }

    private fun downloadWeatherData() {
        runBlocking {
            val hourlyWeather =  WeatherApi.getHourlyForecast(27.937829582F, -82.238832378F) /*WeatherApi.retrofitService.getForecast(27.937829582F, -82.238832378F)*/
            _currentWeather.value = DailyWeather(hourlyWeather)
        }

        viewModelScope.launch {
            while (true) {
                val hourlyWeather = WeatherApi.getHourlyForecast(
                    27.937829582F,
                    -82.238832378F
                ) /*WeatherApi.retrofitService.getForecast(27.937829582F, -82.238832378F)*/
                _currentWeather.value = DailyWeather(hourlyWeather)

                delay(1.minutes)

                Log.d("Hej", "Updated")
            }
        }
    }
}