package com.application.weatherapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.weatherapp.R
import com.application.weatherapp.model.DailyWeather
import com.application.weatherapp.model.Weather
import com.application.weatherapp.model.WeatherType
import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.viewmodel.DateTimeViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
import java.time.LocalDateTime
import kotlin.streams.toList

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
        currentWeather: DailyWeather?,
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
                currentWeather = currentWeather ?: DailyWeather(
                    LocalDateTime.now(),
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
        currentWeather: DailyWeather,
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
        Surface(
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                ) {
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

                CurrentWeatherHourlyForecastWidget(
                    modifier = Modifier
                        .padding(
                            top = 400.dp,
                            start = 16.dp,
                            bottom = 10.dp
                        )
                )
            }
        }
    }

    val test = mutableListOf(
        Weather(LocalDateTime.now(), 10F, 12F, WeatherType.CLOUDY),
        Weather(LocalDateTime.now().plusHours(1), 13F, 9F, WeatherType.SUNNY),
        Weather(LocalDateTime.now().plusHours(2), 10F, 11F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(3), 10F, 4F, WeatherType.THUNDERSTORM),
        Weather(LocalDateTime.now().plusHours(4), 10F, 2F, WeatherType.PARTLY_CLOUDY_DAY),
        Weather(LocalDateTime.now().plusHours(5), 10F, 10F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(6), 10F, 20F, WeatherType.TORNADO),
        Weather(LocalDateTime.now().plusHours(7), 10F, 22F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(8), 10F, 12F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(9), 10F, 4F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(10), 10F, 1F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(11), 10F, 40F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(12), 10F, 12F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(13), 10F, 15F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(14), 10F, 19F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(15), 10F, 20F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(16), 10F, 22F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(17), 10F, 12F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(18), 10F, 14F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(19), 10F, 9F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(20), 10F, 10F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(21), 10F, 0F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(22), 10F, 8F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(23), 10F, 10F, WeatherType.SNOWY)
    )

    private fun calculateYCoordinate(
        maxValue: Float,
        currentValue: Float,
        canvasHeight: Float
    ): Float {
        val maxAndCurrentValueDifference = (maxValue - currentValue)

        val relativePercentageOfScreen = (canvasHeight / maxValue)

        return maxAndCurrentValueDifference * relativePercentageOfScreen
    }

    @Composable
    fun CurrentWeatherHourlyForecastWidget(
        modifier: Modifier = Modifier,
        currentWeather: DailyWeather = DailyWeather(
            LocalDateTime.now(),
            10F,
            10F,
            WeatherType.SUNNY
        )
    ) {
        currentWeather.hourlyForecast = test

        val maxValue =
            currentWeather.hourlyForecast!!.stream().map { it.currentTemperature }.toList()
                .maxOrNull() ?: 0F

        var columnSize by remember { mutableStateOf(Size.Zero) }

        val color = MaterialTheme.colorScheme.onPrimary

        LazyRow(
            modifier = modifier
        ) {
            itemsIndexed(currentWeather.hourlyForecast!!) { index, weather ->

                Column(modifier = Modifier) {
                    Canvas(modifier = Modifier) {
                        val startX = 0F
                        val endX = startX + columnSize.width

                        var nextWeatherTemperature =
                            currentWeather.hourlyForecast!!.first().currentTemperature

                        if (currentWeather.hourlyForecast!!.lastIndex >= index + 1)
                            nextWeatherTemperature =
                                currentWeather.hourlyForecast!![index + 1].currentTemperature

                        drawLine(
                            start = Offset(
                                x = startX,
                                y = calculateYCoordinate(
                                    maxValue,
                                    weather.currentTemperature,
                                    50.dp.toPx()
                                )
                            ),
                            end = Offset(
                                x = endX,
                                y = calculateYCoordinate(
                                    maxValue,
                                    nextWeatherTemperature,
                                    50.dp.toPx()
                                )
                            ),
                            color = color,
                            strokeWidth = Stroke.DefaultMiter
                        )
                    }

                    Column(modifier = Modifier
                        .padding(top = 90.dp)
                        .onGloballyPositioned {
                            columnSize = it.size.toSize()
                        }
                    ) {
                        Icon(
                            painter = getWeatherIconPainter(weatherType = weather.weatherType),
                            contentDescription = weather.weatherDescription,
                            modifier = Modifier
                                .size(48.dp)
                        )

                        Text(
                            text = "${weather.date.hour}",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(CenterHorizontally)
                        )
                    }
                }
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