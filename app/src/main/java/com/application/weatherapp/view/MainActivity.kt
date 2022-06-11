package com.application.weatherapp.view

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.weatherapp.R
import com.application.weatherapp.model.Weather
import com.application.weatherapp.model.WeatherType
import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.viewmodel.DateTimeViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
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
                containerColor = MaterialTheme.colorScheme.onPrimary,
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
        currentWeather: Weather?,
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
                currentWeather = currentWeather ?: Weather(
                    LocalDate.now(),
                    0F,
                    0F,
                    0F,
                    0F,
                    WeatherType.SUNNY
                ),
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
                    painter = getWeatherIconPainter(weatherType = currentWeather.weatherType),
                    contentDescription = currentWeather.weatherDescription,
                    modifier = Modifier
                        .size(100.dp)
                )

                Text(
                    text = currentWeather.weatherDescription,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun getWeatherIconPainter(weatherType: WeatherType): Painter {
        return when (weatherType) {
            WeatherType.WINDY -> painterResource(R.drawable.ic_windy)
            WeatherType.TORNADO -> painterResource(R.drawable.ic_tornado)
            WeatherType.HURRICANE -> painterResource(R.drawable.ic_hurricane)

            WeatherType.CLOUDY -> painterResource(R.drawable.ic_cloudy)
            WeatherType.PARTLY_CLOUDY_DAY -> painterResource(R.drawable.ic_day_partly_cloudy)
            WeatherType.PARTLY_CLOUDY_NIGHT -> painterResource(R.drawable.ic_night_partly_cloudy)
            WeatherType.FOGGY -> painterResource(R.drawable.ic_foggy)

            WeatherType.SUNNY -> painterResource(R.drawable.ic_sunny)
            WeatherType.HAZY -> painterResource(R.drawable.ic_hazy)

            WeatherType.SNOWY -> painterResource(R.drawable.ic_snowy)
            WeatherType.SNOWY_RAINY -> painterResource(R.drawable.ic_snowy_rainy)
            WeatherType.PARTLY_SNOWY -> painterResource(R.drawable.ic_partly_snowy)
            WeatherType.PARTLY_SNOWY_RAINY -> painterResource(R.drawable.ic_partly_snowy_rainy)
            WeatherType.HEAVY_SNOW -> painterResource(R.drawable.ic_heavy_snow)

            WeatherType.RAINY -> painterResource(R.drawable.ic_rainy)
            WeatherType.PARTLY_RAINY -> painterResource(R.drawable.ic_partly_rainy)
            WeatherType.HEAVY_RAIN -> painterResource(R.drawable.ic_heavy_rain)
            WeatherType.THUNDERSTORM -> painterResource(R.drawable.ic_thunderstorm)
            WeatherType.PARTLY_THUNDERSTORM -> painterResource(R.drawable.ic_partly_thunderstorm)
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
    fun Home(
        weatherViewModel: WeatherViewModel = viewModel()
    ) {
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
                currentWeather = weatherViewModel.currentWeather.value
            )
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