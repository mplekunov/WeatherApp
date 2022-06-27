package com.application.weatherapp.network.api.service

import com.application.weatherapp.model.Location
import com.application.weatherapp.network.NetworkInterceptor
import com.application.weatherapp.network.api.GeocoderApi
import com.application.weatherapp.network.api.json.Feature
import com.application.weatherapp.network.api.json.NominatimResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.time.Duration.Companion.seconds

private val BASE_URL = "https://nominatim.openstreetmap.org/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(
        OkHttpClient.Builder()
            .addNetworkInterceptor(NetworkInterceptor())
            .build()
    )
    .baseUrl(BASE_URL)
    .build()

private val QUERY_DELAY = 1.1.seconds

interface NominatimApiService {
    @GET("search?format=geojson&addressdetails=1&accept-language=en")
    suspend fun getLocations(
        @Query("q") address: String,
        @Query("limit") maxSize: Int
    ): NominatimResponse

    @GET("reverse?format=geojson&addressdetails=1&accept-language=en")
    suspend fun getLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ) : NominatimResponse
}

object NominatimApi : GeocoderApi {
    override suspend fun getLocations(address: String, maxSize: Int): List<Location> {
        delay(QUERY_DELAY)

        val response = retrofit.create(NominatimApiService::class.java).getLocations(address, 50)

        val locations = mutableListOf<Location>()

        val qualifiedResponse = response.features
            .filter { feature -> isRequiredType(feature.locationInfo.type) }

        for (i in qualifiedResponse.indices) {
            if (i >= maxSize)
                break

            val feature = qualifiedResponse[i]

            locations.add(feature.toLocation())
        }

        return locations.toList()
    }

    override suspend fun getLocation(latitude: Double, longitude: Double): Location {
        delay(QUERY_DELAY   )

        val response = retrofit.create(NominatimApiService::class.java).getLocation(latitude, longitude)

        return response.features.first().toLocation()
    }

    private fun isRequiredType(type: String): Boolean {
        return when (type) {
            "administrative" -> true
            "city" -> true
            "village" -> true
            "hamlet" -> true
            else -> false
        }
    }

    private fun Feature.toLocation(): Location {
        val city = this.locationInfo.address.city
        val state = this.locationInfo.address.state
        val country = this.locationInfo.address.country

        val longitude = this.locationPosition.coordinates[0]
        val latitude = this.locationPosition.coordinates[1]

        val location = Location(latitude, longitude)

        location.city = city ?: ""
        location.state = state ?: ""
        location.country = country ?: ""

        return location
    }
}