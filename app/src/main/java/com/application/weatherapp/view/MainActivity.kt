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
import com.application.weatherapp.view.weather.*
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
        Surface() {
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
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        ),
                        currentWeather = weatherViewModel.currentWeather.value!!
                    )
                }

                HourlyTemperatureForecastWidget(
                    modifier = Modifier
                        .padding(
                            top = 340.dp,
                            start = 16.dp,
                            bottom = 10.dp,
                            end = 16.dp
                        ),
                    hourlyWeather = weatherViewModel.currentWeather.value!!.hourlyWeather
                )

                Spacer(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(1.dp)
                )

                AdditionalWeatherInfoWidget(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            bottom = 10.dp,
                            end = 16.dp
                        ),
                    currentWeather = weatherViewModel.currentWeather.value
                )

                Spacer(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .height(1.dp)
                )

                HourlyPrecipitationForecastWidget(
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            bottom = 10.dp,
                            end = 16.dp
                        ),
                    hourlyWeather = weatherViewModel.currentWeather.value!!.hourlyWeather
                )
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