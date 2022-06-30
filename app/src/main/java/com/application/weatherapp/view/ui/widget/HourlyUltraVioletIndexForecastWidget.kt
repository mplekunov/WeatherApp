package com.application.weatherapp.view.ui.widget

import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.weatherapp.model.graph.*
import com.application.weatherapp.model.weather.HourlyWeather
import com.application.weatherapp.viewmodel.sample.SampleHourlyWeatherProvider
import java.math.BigDecimal
import java.math.RoundingMode

@Preview
@Composable
private fun PreviewHourlyUltraVioletIndexForecastWidget() {
    HourlyUltraVioletIndexForecastWidget(
        graphSize = DpSize(40.dp, 100.dp),
        modifier = Modifier.fillMaxWidth(),
        hourlyWeather = SampleHourlyWeatherProvider().values.first()
    )
}

@Composable
fun HourlyUltraVioletIndexForecastWidget(
    modifier: Modifier = Modifier,
    graphSize: DpSize,
    hourlyWeather: HourlyWeather
) {
    val fontColor = MaterialTheme.colorScheme.onPrimary
    val fontSize = (graphSize.width.value / 5).sp

    Column(modifier = modifier) {
        Text(
            text = "UV-Index",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        LazyRow(
            modifier = Modifier
        ) {
            itemsIndexed(hourlyWeather.weatherForecast) { index, weather ->
                val currentValue = weather.ultraVioletIndex

                var nextValue =
                    hourlyWeather.weatherForecast.first().ultraVioletIndex

                if (hourlyWeather.weatherForecast.lastIndex >= index + 1)
                    nextValue =
                        hourlyWeather.weatherForecast[index + 1].ultraVioletIndex

                val prevValue =
                    if (index == 0) hourlyWeather.weatherForecast.last().ultraVioletIndex
                    else hourlyWeather.weatherForecast[index - 1].ultraVioletIndex

                val tripleValuePoint = convertToQuadraticConnectionPoints(
                    startValue = prevValue,
                    midValue = currentValue,
                    endValue = nextValue,
                    maxValue = 12F,
                    minValue = 0F,
                    canvasSize = graphSize
                )

                val color = getGraphColor(
                    currentValue,
                    MaterialTheme.colorScheme.onPrimary
                )

                val colors = listOf(
                    color,
                    color.copy(alpha = 0.8f),
                    color.copy(alpha = 0.6f),
                    color.copy(alpha = 0.4f),
                    color.copy(alpha = 0.2f),
                    Color.Transparent
                )

                val brush = Brush.verticalGradient(
                    colors = colors
                )

                Column(modifier = Modifier) {
                    Box {
                        DrawTextInMidOfCurve(
                            modifier = Modifier.offset(y = (-20).dp),
                            text = BigDecimal(currentValue.toString())
                                .setScale(0, RoundingMode.HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString(),
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            fontColor = MaterialTheme.colorScheme.onPrimary,
                            fontSize = fontSize
                        )

                        DrawQuadraticCurve(
                            modifier = Modifier,
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            graphColor = color
                        )

                        DrawAreaUnderQuadraticCurve(
                            tripleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            fillBrush = brush
                        )

                        DrawCurveSideBorders(
                            tupleValuePoint = tripleValuePoint,
                            canvasSize = graphSize,
                            borderBrush = brush
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

private fun getGraphColor(uvIndexValue: Float, color: Color): Color {
    val maxAlpha = 100F
    val minAlpha = 20F

    val fraction = (maxAlpha - minAlpha) / 12

    return color.copy(alpha = (minAlpha + (uvIndexValue * fraction)) / 100)
}