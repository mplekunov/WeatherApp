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

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    /**
     * Gets possible locations from [queryAddress]
     * Updates [locations] if results were found
     */
    fun getLocations(queryAddress: String, maxNumOfResults: Int, geocoder: GeocoderApi) {
        cancelAllJobs()

        if (queryAddress.isNotEmpty()) {
            viewModelScope.launch {
                _isSearching.postValue(true)
                val locations = geocoder.getLocations(queryAddress, maxNumOfResults)
                _isSearching.postValue(false)

                _locations.postValue(locations)
            }
        }
    }

    /**
     * Gets location information at [latitude] [longitude]
     * Updates [locations] if results were found
     */
    fun getLocation(latitude: Double, longitude: Double, geocoder: GeocoderApi) {
        cancelAllJobs()

        viewModelScope.launch {
            _isSearching.postValue(true)
            val location = geocoder.getLocation(latitude, longitude)
            _isSearching.postValue(false)

            _locations.postValue(listOf(location))
        }
    }

    /**
     *  Sets [currentLocation]
     */
    fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }


    private fun cancelAllJobs() {
        viewModelScope.coroutineContext.cancelChildren()
        _isSearching.postValue(false)
    }
}