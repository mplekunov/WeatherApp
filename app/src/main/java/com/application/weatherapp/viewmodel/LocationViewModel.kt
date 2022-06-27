package com.application.weatherapp.viewmodel


import androidx.lifecycle.*
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.GeocoderApi
import kotlinx.coroutines.*

class LocationViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>(emptyList())
    val locations: LiveData<List<Location>> = _locations

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    /**
     * Gets possible locations from [queryAddress]
     * Updates [locations] if results were found
     */
    fun getLocations(queryAddress: String, maxNumOfResults: Int, geocoder: GeocoderApi) {
        viewModelScope.coroutineContext.cancelChildren()

        if (queryAddress.isNotEmpty()) {
            viewModelScope.launch {
                val locations = geocoder.getLocations(queryAddress, maxNumOfResults)

                _locations.postValue(locations)
            }
        }
    }

    /**
     * Gets location information at [latitude] [longitude]
     * Updates [locations] if results were found
     */
    fun getLocation(latitude: Double, longitude: Double, geocoder: GeocoderApi) {
        viewModelScope.coroutineContext.cancelChildren()

        viewModelScope.launch {
            val location = geocoder.getLocation(latitude, longitude)

            _locations.postValue(listOf(location))
        }
    }

    /**
     *  Sets [currentLocation]
     */
    fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }
}