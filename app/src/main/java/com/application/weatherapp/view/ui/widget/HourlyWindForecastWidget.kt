package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.R
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode

@Preview
@Composable
private fun PreviewHourlyWindForecastWidget() {
    HourlyWindForecastWidget(
        graphSize = DpSize(40.dp, 100.dp),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyWindForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: DpSize,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val windSpeedFontSize = 40.sp
    val fontSize = (graphSize.width.value / 5).sp

    Column(modifier = modifier) {
        Text(
            text = "Wind",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Row(Modifier.padding(bottom = 20.dp)) {
            Text(
                text = "${hourlyWeather.weatherForecast.first().wind.speed.value.toInt()}",
                fontSize = windSpeedFontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )

            Text(
                text = hourlyWeather.weatherForecast.first().wind.speed.unit.unit,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(start = 4.dp, bottom = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .height(windSpeedFontSize.value.dp)
                    .align(Alignment.CenterVertically)
                    .background(Color.Gray)
                    .width(2.dp)
            )

            DirectionIcon(
                modifier = Modifier
                    .height(windSpeedFontSize.value.dp)
                    .align(Alignment.CenterVertically),
                direction = hourlyWeather.weatherForecast.first().wind.direction.value + 180,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F)
            )
        }

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.wind.speed.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().wind.speed.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].wind.speed.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().wind.speed.value
                    else hourlyWeather.weatherForecast[index - 1].wind.speed.value

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = hourlyWeather.maxWindSpeed.speed.value,
                    minValue = hourlyWeather.minWindSpeed.speed.value,
                    canvasSize = graphSize
                )

                val colors = listOf(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                    Color.Transparent
                )

                val brush = Brush.verticalGradient(
                    colors = colors
                )

                Column(modifier = Modifier) {
                    DirectionIcon(
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.CenterHorizontally)
                            .size(graphSize.width / 2),
                        direction = hourlyWeather.weatherForecast[index].wind.direction.value + 180,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8F)
                    )

                    Box {
                        DrawTextInMidOfCurve(
                            modifier = Modifier.offset(y = (-20).dp),
                            tripleValuePoint = tripleValuePoint,
                            text = BigDecimal(currentValue.toString())
                                .setScale(1, RoundingMode.HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString(),
                            canvasSize = graphSize,
                            fontColor = MaterialTheme.colorScheme.onPrimary,
                            fontSize = fontSize
                        )


                        DrawQuadraticCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            graphColor = MaterialTheme.colorScheme.onPrimary
                        )

                        DrawCurveSideBorders(
                            tupleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            borderBrush = brush
                        )
                    }

                    Text(
                        text = "${weather.date.hour}",
                        fontSize = fontSize,
                        color = fontColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun DirectionIcon(
    modifier: Modifier = Modifier,
    direction: Float,
    color: Color
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_arrow),
        contentDescription = "",
        modifier = modifier.rotate(direction),
        tint = color
    )
}
