package com.application.weatherapp.network.api

import com.application.weatherapp.model.Location

interface GeocoderApi {
    suspend fun getLocations(address: String, maxSize: Int): List<Location>
    suspend fun getLocation(latitude: Double, longitude: Double): Location
}