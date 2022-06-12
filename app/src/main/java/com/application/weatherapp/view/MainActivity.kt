package com.application.weatherapp.view

import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
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
        Weather(LocalDateTime.now(), 10F, 23F, WeatherType.CLOUDY),
        Weather(LocalDateTime.now().plusHours(1), 24F, 24F, WeatherType.SUNNY),
        Weather(LocalDateTime.now().plusHours(2), 26F, 26F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(3), 27F, 27F, WeatherType.THUNDERSTORM),
        Weather(LocalDateTime.now().plusHours(4), 29F, 29F, WeatherType.PARTLY_CLOUDY_DAY),
        Weather(LocalDateTime.now().plusHours(5), 28F, 28F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(6), 28F, 28F, WeatherType.TORNADO),
        Weather(LocalDateTime.now().plusHours(7), 29F, 29F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(8), 30F, 30F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(9), 29F, 29F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(10), 29F, 29F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(11), 29F, 29F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(12), 28F, 28F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(13), 27F, 27F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(14), 26F, 26F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(15), 26F, 26F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(16), 25F, 25F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(17), 24F, 24F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(18), 24F, 24F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(19), 24F, 24F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(20), 23F, 23F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(21), 23F, 23F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(22), 23F, 23F, WeatherType.SNOWY),
        Weather(LocalDateTime.now().plusHours(23), 24F, 24F, WeatherType.SNOWY)
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

        var color = MaterialTheme.colorScheme.onPrimary

        LazyRow(
            modifier = modifier
        ) {
            itemsIndexed(currentWeather.hourlyForecast!!) { index, weather ->

                Column(modifier = Modifier) {
                    Canvas(modifier = Modifier) {
                        val currentWeatherTemperature = weather.currentTemperature

                        var nextWeatherTemperature =
                            currentWeather.hourlyForecast!!.first().currentTemperature

                        if (currentWeather.hourlyForecast!!.lastIndex >= index + 1)
                            nextWeatherTemperature =
                                currentWeather.hourlyForecast!![index + 1].currentTemperature

                        val prevWeatherTemperature =
                            if (index == 0) currentWeather.hourlyForecast!!.last().currentTemperature
                            else currentWeather.hourlyForecast!![index - 1].currentTemperature

                        val startX = 0F
                        val endX = startX + columnSize.width
                        val midX = endX / 2

                        val startY = calculateYCoordinate(
                            maxValue,
                            (prevWeatherTemperature + currentWeatherTemperature) / 2,
                            280.dp.toPx()
                        )

                        val midY = calculateYCoordinate(
                            maxValue,
                            currentWeatherTemperature,
                            280.dp.toPx()
                        )

                        val endY = calculateYCoordinate(
                            maxValue,
                            (nextWeatherTemperature + currentWeatherTemperature) / 2,
                            280.dp.toPx()
                        )

                        val conPoint1 = PointF((midX + startX) / 2, startY)
                        val conPoint2 = PointF((midX + startX) / 2, midY)

                        val conPoint3 = PointF((endX + midX) / 2, midY)
                        val conPoint4 = PointF((endX + midX) / 2, endY)

                        val filledPath = Path().apply {
                            moveTo(startX, 80.dp.toPx())
                            lineTo(startX, startY)
                            cubicTo(
                                conPoint1.x, conPoint1.y,
                                conPoint2.x, conPoint2.y,
                                midX, midY
                            )
                            cubicTo(
                                conPoint3.x, conPoint3.y,
                                conPoint4.x, conPoint4.y,
                                endX, endY
                            )
                            lineTo(endX, 80.dp.toPx())
                            close()
                        }

                        val graphPath = Path().apply {
                            moveTo(startX, startY)
                            cubicTo(
                                conPoint1.x, conPoint1.y,
                                conPoint2.x, conPoint2.y,
                                midX, midY
                            )
                            cubicTo(
                                conPoint3.x, conPoint3.y,
                                conPoint4.x, conPoint4.y,
                                endX, endY
                            )
                        }

                        drawPath(
                            path = filledPath,
                            color = Color.Gray.copy(alpha = 0.20F),
                            style = Fill
                        )

                        drawPath(
                            path = graphPath,
                            color = color,
                            style = Stroke(2F)
                        )

                        drawLine(
                            start = Offset(startX, 80.dp.toPx()),
                            end = Offset(startX, startY),
                            color = color
                        )

                        drawLine(
                            start = Offset(midX, 80.dp.toPx()),
                            end = Offset(midX, midY),
                            color = color,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 10f), 0f)
                        )

                        drawLine(
                            start = Offset(endX, 80.dp.toPx()),
                            end = Offset(endX, endY),
                            color = color
                        )

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                "${currentWeatherTemperature.toInt()}",
                                midX,
                                midY - 10,
                                Paint().apply {
                                    textSize = 20F
                                    textAlign = Paint.Align.CENTER
                                }
                            )
                        }
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