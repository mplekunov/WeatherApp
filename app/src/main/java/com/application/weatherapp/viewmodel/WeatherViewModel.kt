package com.application.weatherapp.viewmodel

import androidx.lifecycle.*
import com.application.weatherapp.model.Location
import com.application.weatherapp.model.weather.DailyWeather
import com.application.weatherapp.network.api.WeatherApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<DailyWeather>()
    val currentWeather: LiveData<DailyWeather> = _currentWeather

    fun downloadWeatherData(location: Location, weatherApi: WeatherApi) {
        viewModelScope.coroutineContext.cancelChildren()

        viewModelScope.launch {
            while (true) {
                val hourlyWeather = weatherApi.getHourlyForecast(
                    location.latitude,
                    location.longitude
                )

                _currentWeather.value = DailyWeather(hourlyWeather)
                _currentWeather.value = _currentWeather.value

                delay(10.seconds)
            }
        }
    }
}