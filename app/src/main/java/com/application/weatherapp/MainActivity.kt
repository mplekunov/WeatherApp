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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.ui.theme.WeatherAppTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
                containerColor = MaterialTheme.colorScheme.surface
            ),
            placeholder = {
                Text(stringResource(R.string.placeholder_search))
            },
            shape = RoundedCornerShape(4.dp)
        )
    }

    @Composable
    fun WeatherOverview(
        currentWeather: Weather,
        modifier: Modifier
    ) {
        Column {
            CurrentDateOverview(modifier)

            CurrentWeatherOverview(
                currentWeather,
                modifier
            )
        }
    }

    @Composable
    fun CurrentWeatherOverview(
        currentWeather: Weather,
        modifier: Modifier
    ) {

        Text(
            text = "Day ${currentWeather.dayTemperature.toInt()} Night ${currentWeather.nightTemperature.toInt()}",
            modifier = modifier
        )
        Box(
            modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        ) {
            Column(modifier.align(BottomStart)) {
                Text(
                    text = "${currentWeather.currentTemperature.toInt()}",
                    fontSize = 86.sp,
                )

                Text(
                    text = "Feels like ${currentWeather.feelingTemperature.toInt()}",
                    fontSize = 12.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
            }

            Column(modifier.align(BottomEnd)) {
                Icon(
                    painter = painterResource(R.drawable.ic_rainy),
                    contentDescription = "Rainy",
                    modifier = Modifier
                        .size(100.dp)
                )

                Text(
                    text = "Rain",
                    fontSize = 12.sp,
                    modifier = Modifier.align(CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun CurrentDateOverview(
        modifier: Modifier
    ) {
        Box(
            modifier
                .fillMaxWidth()
        ) {
            Text(
                text = LocalDateTime.now().format(
                    DateTimeFormatter.ofLocalizedDate(
                        FormatStyle.LONG
                    )
                )
            )

            Text(
                text = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm")
                ),
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

            WeatherOverview(
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