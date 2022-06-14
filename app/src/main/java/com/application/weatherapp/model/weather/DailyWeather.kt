package com.application.weatherapp.model.weather

import com.application.weatherapp.model.Temperature
import com.application.weatherapp.model.TemperatureUnit
import com.application.weatherapp.model.weather.statistics.*
import java.time.LocalDateTime

class DailyWeather(
    date: LocalDateTime,
    feelingTemperature: Temperature,
    currentTemperature: Temperature,
    weatherType: WeatherType,
    val hourlyWeather: HourlyWeather
) : Weather(date, feelingTemperature, currentTemperature, weatherType) {

    var dayTemperature: Temperature = Temperature(0F, TemperatureUnit.CELSIUS)
    var nightTemperature: Temperature = Temperature(0F, TemperatureUnit.CELSIUS)

    var cloudCover: CloudCover = CloudCover(0F, CloudCoverUnit.PERCENTAGE)
    var humidity: Humidity = Humidity(0F, HumidityUnit.PERCENTAGE)
    var pressure: Pressure = Pressure(0F, PressureUnit.MILLI_BAR)
    var dewPoint: DewPoint = DewPoint(Temperature(0F, TemperatureUnit.CELSIUS))
}