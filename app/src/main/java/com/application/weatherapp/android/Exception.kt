package com.application.weatherapp.android

class PermissionRequiredException(message: String) : Exception(message) {
    constructor() : this("")
}