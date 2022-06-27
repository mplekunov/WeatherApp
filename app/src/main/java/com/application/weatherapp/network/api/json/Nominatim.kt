package com.application.weatherapp.network.api.json

import com.squareup.moshi.Json

data class NominatimResponse(
    @Json(name = "features") val features: List<Feature>
)

data class Feature(
    @Json(name = "properties") val locationInfo: Location,
    @Json(name = "geometry") val locationPosition: Position
)

data class Location(
    @Json(name = "address") val address: Address,
    @Json(name = "type") val type: String
)

data class Address(
    @Json(name = "city") val city: String?,
    @Json(name = "locality") val locality: String?,
    @Json(name = "hamlet") val hamlet: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "country") val country: String?
)

data class Position(
    @Json(name = "coordinates") val coordinates: List<Double>
)