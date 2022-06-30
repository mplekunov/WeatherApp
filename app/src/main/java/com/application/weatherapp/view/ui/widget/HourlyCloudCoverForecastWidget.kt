package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.ui.theme.Typography
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

@Preview
@Composable
private fun PreviewHourlyCloudCoverForecastWidget() {
    HourlyCloudCoverForecastWidget(
        graphSize = DpSize(50.dp, 100.dp),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyCloudCoverForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: DpSize,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val fontSize = (graphSize.width.value / 5).sp

    Column(modifier = modifier) {
        Text(
            text = "Cloud cover",
            style = Typography.titleMedium,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.cloudCover.value

                var nextValue =
                    hourlyWeather.weatherForecast.first().cloudCover.value

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].cloudCover.value

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().cloudCover.value
                    else hourlyWeather.weatherForecast[index - 1].cloudCover.value

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = hourlyWeather.maxCloudCover.value,
                    minValue = hourlyWeather.minCloudCover.value,
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
                    Box {
                        DrawTextInMidOfCurve(
                            modifier = Modifier.offset(y = (-20).dp),
                            tripleValuePoint = tripleValuePoint,
                            text = BigDecimal(currentValue.toString())
                                .setScale(0, RoundingMode.HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString() + " ${weather.cloudCover.unit.unit}",
                            canvasSize = graphSize,
                            fontColor = MaterialTheme.colorScheme.onPrimary,
                            fontSize = fontSize
                        )

                        DrawQuadraticCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            graphColor = MaterialTheme.colorScheme.onPrimary
                        )

                        DrawAreaUnderQuadraticCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            fillBrush = brush
                        )
                    }

                    Text(
                        text = "${weather.date.hour}",
                        color = fontColor,
                        fontSize = fontSize,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}