package com.application.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.GeocoderApi
import kotlinx.coroutines.*

class LocationViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>(emptyList())
    val locations: LiveData<List<Location>> = _locations

    fun searchForLocations(queryAddress: String, maxNumOfResults: Int, geocoder: GeocoderApi) {
        viewModelScope.coroutineContext.cancelChildren()

        viewModelScope.launch {
            val locations = geocoder.getLocations(queryAddress, maxNumOfResults)

            _locations.postValue(locations)

            Log.d("LocationViewModel", queryAddress)
        }
    }
}