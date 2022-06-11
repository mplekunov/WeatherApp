package com.application.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.viewmodel.DateTimeViewModel
import java.time.LocalDate

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
    fun SearchLocationBar(
        modifier: Modifier,
        onValueChange: (String) -> Unit
    ) {
        TextField(
            value = "",
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 16.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location"
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(stringResource(R.string.placeholder_search))
            },
            shape = RoundedCornerShape(4.dp)
        )
    }

    @Composable
    fun CurrentWeatherWidget(
        currentWeather: Weather,
        modifier: Modifier,
        viewModel: DateTimeViewModel = viewModel()
        ) {
        val currentDate = viewModel.date.observeAsState()
        val currentTime = viewModel.time.observeAsState()

        Column {
            DateTimeOverview(
                date = currentDate.value ?: "June 5, 2022",
                time = currentTime.value ?: "10:55",
                modifier = modifier
            )

            CurrentWeatherOverview(
                currentWeather = currentWeather,
                modifier = modifier
            )
        }
    }

    @Composable
    fun CurrentWeatherOverview(
        currentWeather: Weather,
        modifier: Modifier
    ) {
        // Day/Night Temperatures
        Text(
            text = "Day ${currentWeather.dayTemperature.toInt()} Night ${currentWeather.nightTemperature.toInt()}",
            modifier = modifier
        )

        // Current Weather Info
        Box(
            modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        ) {
            // First Column is current/feeling temperature
            Column(
                modifier
                    .align(BottomStart)
            ) {
                Text(
                    text = "${currentWeather.currentTemperature.toInt()}",
                    fontSize = 86.sp,
                )

                Text(
                    text = "Feels like ${currentWeather.feelingTemperature.toInt()}",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(CenterHorizontally)
                )
            }

            // Second Column is Weather Forecast (Icon/Description)
            Column(
                modifier
                    .align(BottomEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_rainy),
                    contentDescription = "Rainy",
                    modifier = Modifier
                        .size(100.dp)
                )

                Text(
                    text = "Rain",
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun DateTimeOverview(
        date: String,
        time: String,
        modifier: Modifier
    ) {
        //
        Box(
            modifier
                .fillMaxWidth()
        ) {
            Text(
                text = date
            )

            Text(
                text = time,
                modifier = Modifier.align(CenterEnd)
            )
        }
    }

    @Composable
    fun Home() {
        Column {
            SearchLocationBar(
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
                currentWeather = weatherTest
            )
        }
    }

    val weatherTest = Weather(LocalDate.now(), 10F, 20F, 15F, 18F, WeatherType.Rainy)

    @Preview
    @Composable
    fun HomePreview() {
        WeatherAppTheme {
            Home()
        }
    }
}