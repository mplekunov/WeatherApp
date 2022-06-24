package com.application.weatherapp.network.api

import com.application.weatherapp.model.Location

interface GeocoderApi {
    suspend fun getLocations(address: String, maxSize: Int): List<Location>
}