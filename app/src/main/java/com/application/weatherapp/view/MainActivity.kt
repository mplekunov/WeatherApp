package com.application.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*

import androidx.compose.ui.unit.dp
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.service.MetNorwayApi
import com.application.weatherapp.network.api.service.NominatimApi

import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.view.ui.LocationSearchBar
import com.application.weatherapp.view.ui.widget.*
import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationViewModel: LocationViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var homeLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            WeatherAppTheme {
                PermissionScreen()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun onPermissionReceived() {
        val currentLocation =
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)

        currentLocation.addOnCompleteListener {
            homeLocation = Location(it.result.latitude, it.result.longitude)

            locationViewModel.searchForLocation(
                homeLocation.latitude,
                homeLocation.longitude,
                NominatimApi
            )

            weatherViewModel.currentWeather.observe(this) {
                setContent {
                    WeatherAppTheme {
                        Home(
                            weatherViewModel,
                            locationViewModel
                        )
                    }
                }
            }

            locationViewModel.locations.observe(this) {
                weatherViewModel.downloadWeatherData(homeLocation, MetNorwayApi)
                locationViewModel.setCurrentLocation(homeLocation)
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("MissingPermission")
    @Composable
    private fun PermissionScreen() {
        val locationPermission = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        if (locationPermission.allPermissionsGranted)
            onPermissionReceived()
        else {
            Surface(
                color = Color.Magenta,
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    Text(text = "Give me your location you weirdo... I wanna know where you are... he he he")

                    Button(
                        onClick = {
                           locationPermission.launchMultiplePermissionRequest()
                        }
                    ) {
                        Text(text = "GIVE ME LOCATION")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    private fun Home(
        weatherViewModel: WeatherViewModel,
        locationViewModel: LocationViewModel
    ) {
        val dailyWeather = weatherViewModel.currentWeather.observeAsState()

        val focusRequester = remember { FocusRequester() }

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

        Surface(modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusRequester.requestFocus()
            }
            .focusRequester(focusRequester)
            .focusable()
        ) {
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
                            .fillMaxWidth(),
                        locationViewModel = locationViewModel,
                        weatherViewModel = weatherViewModel
                    )

                    CurrentWeatherOverviewWidget(
                        modifier = widgetModifier,
                        dailyWeather = dailyWeather.value!!
                    )
                }

                HourlyTemperatureForecastWidget(
                    modifier = widgetModifier.padding(top = 350.dp),
                    graphSize = Size(60F, 200F),
                    hourlyWeather = dailyWeather.value!!.hourlyWeather
                )

                Spacer(modifier = spacerModifier)

                CurrentWeatherExtendedInfoWidget(
                    modifier = widgetModifier,
                    dailyWeather = dailyWeather.value!!
                )

                Spacer(modifier = spacerModifier)

                HourlyPrecipitationForecastWidget(
                    modifier = widgetModifier,
                    graphSize = Size(50F, 100F),
                    hourlyWeather = dailyWeather.value!!.hourlyWeather
                )

                Spacer(modifier = spacerModifier)

                HourlyWindForecastWidget(
                    modifier = widgetModifier,
                    graphSize = Size(60F, 100F),
                    hourlyWeather = dailyWeather.value!!.hourlyWeather
                )

                Spacer(modifier = spacerModifier)
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun askLocationPermission() {
        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        if (!locationPermissionsState.allPermissionsGranted) {
            LaunchedEffect(null) {
                locationPermissionsState.launchMultiplePermissionRequest()
            }
        }
    }
}

