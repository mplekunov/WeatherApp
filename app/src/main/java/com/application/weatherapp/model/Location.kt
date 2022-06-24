package com.application.weatherapp.model

class Location(
    var latitude: Double,
    var longitude: Double,
) {
    var city: String = ""
    var state: String = ""
    var country: String = ""

    override fun equals(other: Any?): Boolean
        = (other is Location)
            && other.city == this.city
            && other.state == this.state
            && other.country == this.country

    override fun hashCode(): Int {
        var result = city.hashCode()
        result *= 31 + state.hashCode()
        result *= 31 + country.hashCode()
        return result
    }

    override fun toString(): String {
        val address = StringBuilder()

        if (city.isNotEmpty() && state.isNotEmpty() && country.isNotEmpty())
            address.append(city).append(", ").append(state).append(", ").append(country)
        else if (state.isNotEmpty() && country.isNotEmpty())
            address.append(state).append(", ").append(country)
        else
            address.append(country)

        return address.toString()
    }
}