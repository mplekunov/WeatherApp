package com.application.weatherapp.view.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.application.weatherapp.view.ui.LocationSearchBar
import com.application.weatherapp.view.ui.widget.*
import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(
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
        ) { focusRequester.requestFocus() }
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