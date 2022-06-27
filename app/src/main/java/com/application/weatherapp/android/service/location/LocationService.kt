package com.application.weatherapp.android.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.application.weatherapp.android.PermissionRequiredException
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.GeocoderApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val locationPermission =
    listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

private fun checkPermission(context: Context): Boolean {
    val it = locationPermission.iterator()
    while (it.hasNext()) {
        if (ContextCompat.checkSelfPermission(
                context,
                it.next()
            ) == PackageManager.PERMISSION_DENIED
        )
            return false
    }

    return true
}

object LocationService {
    @Throws(PermissionRequiredException::class)
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context, geocoderProvider: GeocoderApi) : Location {
        if (!checkPermission(context))
            throw PermissionRequiredException("Operation requires Location permission!")

        val deferredLocation = withContext(Dispatchers.IO) {
            val fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(context)
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)

            // Better than nothing for the time being... though it looks bad
            while (!fusedLocationClient.isComplete) {}

            fusedLocationClient.result
        }

        return geocoderProvider.getLocation(deferredLocation.latitude, deferredLocation.longitude)
    }
}