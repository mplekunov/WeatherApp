package com.application.weatherapp.network.api

import okhttp3.Interceptor
import okhttp3.Response

class WeatherNetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request()
                .newBuilder()
                .header("User-Agent", "Weather App")
                .build()
        )
    }
}