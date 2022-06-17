package com.application.weatherapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.view.ui.weather.*
import com.application.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Home()
            }
        }
    }

    @Composable
    private fun Home(
        weatherViewModel: WeatherViewModel = viewModel()
    ) {
        val widgetModifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
            .fillMaxWidth()

        val spacerModifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .background(Color.Gray)
            .height(1.dp)

        Surface {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                ) {
                    LocationSearchBar(
                        modifier = Modifier
                            .padding(all = 8.dp)
                    ) {}

                    CurrentWeatherWidget(
                        modifier = widgetModifier,
                        dailyWeather = weatherViewModel.currentWeather.value!!
                    )
                }

                HourlyTemperatureForecastWidget(
                    modifier = widgetModifier.padding(top = 340.dp),
                    hourlyWeather = weatherViewModel.currentWeather.value!!.hourlyWeather
                )

                Spacer(modifier = spacerModifier)

                CurrentWeatherExtendedInfoWidget(
                    modifier = widgetModifier,
                    dailyWeather = weatherViewModel.currentWeather.value
                )

                Spacer(modifier = spacerModifier)

                HourlyPrecipitationForecastWidget(
                    modifier = widgetModifier,
                    hourlyWeather = weatherViewModel.currentWeather.value!!.hourlyWeather
                )

                Spacer(modifier = spacerModifier)
            }
        }
    }

    @Preview
    @Composable
    fun HomePreview() {
        WeatherAppTheme {
            Home()
        }
    }
}