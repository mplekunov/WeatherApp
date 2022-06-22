package com.application.weatherapp.error

class ForecastRefreshError() : Throwable() {
    constructor(msg: String, error: Throwable) : this() {

    }
}
