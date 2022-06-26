package com.application.weatherapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.view.ui.screen.PermissionScreen
import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    private val locationViewModel: LocationViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherAppTheme {
                PermissionScreen(
                    weatherViewModel,
                    locationViewModel
                )
            }
        }
    }
}

