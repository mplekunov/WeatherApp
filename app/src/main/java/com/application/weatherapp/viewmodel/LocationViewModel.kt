package com.application.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.GeocoderApi
import kotlinx.coroutines.*

class LocationViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>(emptyList())
    val locations: LiveData<List<Location>> = _locations

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    fun searchForLocations(queryAddress: String, maxNumOfResults: Int, geocoder: GeocoderApi) {
        viewModelScope.coroutineContext.cancelChildren()
        _isSearching.postValue(false)


        if (queryAddress.isNotEmpty()) {
            viewModelScope.launch {

                _isSearching.postValue(true)
                val locations = geocoder.getLocations(queryAddress, maxNumOfResults)
                _isSearching.postValue(false)
                _locations.postValue(locations)
            }
        }
    }

    fun searchForLocation(latitude: Double, longitude: Double, geocoder: GeocoderApi) {
        viewModelScope.coroutineContext.cancelChildren()

        viewModelScope.launch {

            _isSearching.postValue(true)
            val location = geocoder.getLocation(latitude, longitude)
            _isSearching.postValue(false)
            _locations.postValue(listOf(location))
        }
    }
}