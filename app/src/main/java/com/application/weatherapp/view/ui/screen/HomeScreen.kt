package com.application.weatherapp.view.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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

    val density = LocalDensity.current

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var overviewWidgetHeight by remember { mutableStateOf(0.dp) }
    var temperatureWidgetHeight by remember { mutableStateOf(0.dp) }

    val spacerHeight = screenHeight - overviewWidgetHeight - temperatureWidgetHeight - with(density) { 20f.toDp() }

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
        .fillMaxSize()
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
                    .onGloballyPositioned {
                        overviewWidgetHeight = with(density) { it.size.height.toDp() }
                    }
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(spacerHeight)
                    .background(Color.Transparent)
            )

            HourlyTemperatureForecastWidget(
                modifier = widgetModifier
                    .onGloballyPositioned {
                        temperatureWidgetHeight = with(density) { it.size.height.toDp() }
                    },
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