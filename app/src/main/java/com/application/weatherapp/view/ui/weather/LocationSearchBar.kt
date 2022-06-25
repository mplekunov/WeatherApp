package com.application.weatherapp.view.ui.weather

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewModelScope
import com.application.weatherapp.R
import com.application.weatherapp.network.api.service.MetNorwayApi
import com.application.weatherapp.network.api.service.NominatimApi

import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LocationSearchBar(
    modifier: Modifier,
    locationViewModel: LocationViewModel,
    weatherViewModel: WeatherViewModel
) {
    var textAddress by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    var textFieldY by remember { mutableStateOf(0) }
    var textFieldX by remember { mutableStateOf(0) }

    val locations = locationViewModel.locations.observeAsState()

    val isSearching = locationViewModel.isSearching.observeAsState()

    val density = LocalDensity.current

    val moveAnimation by rememberInfiniteTransition().animateValue(
        initialValue = 0.dp,
        targetValue = 300.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    TextField(
        value = textAddress,
        onValueChange = {
            textAddress = it

            locationViewModel.searchForLocations(textAddress, 5, NominatimApi)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                hasFocus = focusState.hasFocus
            }
            .focusRequester(focusRequester)
            .focusable()
            .onGloballyPositioned { textField ->
                textFieldY = textField.positionInParent().y.toInt() + textField.size.height
                textFieldX = textField.positionInParent().x.toInt()
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        shape = RoundedCornerShape(12.dp)
    )

    if (isSearching.value!!) {
        Box(Modifier.padding(start = 16.dp, end = 16.dp)) {
            Surface(
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray)
                )
            }

            Surface(
                modifier = Modifier.offset(x = moveAnimation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(
                    Modifier
                        .width(80.dp)
                        .height(4.dp)
                        .background(Color.DarkGray)
                )
            }
        }
    }

    Popup(
        alignment = Alignment.TopStart,
        offset = IntOffset(textFieldX, textFieldY)
    ) {
        AnimatedVisibility(
            visible = hasFocus,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 400)
            ) {
                with(density) { 0.dp.roundToPx() }
            } + expandVertically(
                animationSpec = tween(durationMillis = 400)
            ) + scaleIn(
                animationSpec = tween(durationMillis = 400)
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 400)
            ) {
                with(density) { 0.dp.roundToPx() }
            } + shrinkVertically(
                animationSpec = tween(durationMillis = 400)
            ) + scaleOut(
                animationSpec = tween(durationMillis = 400)
            )
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        clip = true,
                        ambientColor = Color.Gray,
                        spotColor = Color.Gray
                    ),
                color = Color.DarkGray,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column {
                    for (i in locations.value!!.indices) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            LocationText(
                                location = locations.value!![i],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        weatherViewModel.downloadWeatherData(
                                            locations.value!![i],
                                            MetNorwayApi
                                        )

                                        textAddress = locations.value!![i].toString()

                                        focusManager.clearFocus()
                                    }
                                    .padding(
                                        start = 8.dp,
                                        end = 8.dp,
                                        top = 8.dp,
                                        bottom = 8.dp
                                    )
                            )

                            if (i != locations.value!!.lastIndex) {
                                Spacer(
                                    modifier = Modifier
                                        .padding(
                                            top = 4.dp,
                                            bottom = 4.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        )
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color.Gray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


//Row {
//    Icon(
//        imageVector = Icons.Default.Warning,
//        contentDescription = "Location",
//        tint = MaterialTheme.colorScheme.onTertiary,
//        modifier = Modifier
//            .align(Alignment.CenterVertically)
//            .padding(12.dp)
//    )
//
//    Text(
//        text = "No results for \"$textAddress\"!",
//        modifier = Modifier
//            .padding(
//                start = 8.dp,
//                end = 8.dp,
//                top = 8.dp,
//                bottom = 8.dp
//            )
//    )
//}
@Composable
private fun LocationText(
    location: com.application.weatherapp.model.Location,
    modifier: Modifier = Modifier
) {
    val headerText = if (location.city.isNotEmpty())
        location.city
    else if (location.state.isNotEmpty())
        location.state
    else
        location.country

    val address = StringBuilder()

    if (location.state.isNotEmpty() && location.country.isNotEmpty())
        address.append(location.state).append(", ").append(location.country)
    else if (location.state.isEmpty())
        address.append(location.country)

    Column(modifier = modifier) {
        Row {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(12.dp)
            )

            Column {
                Text(
                    text = headerText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )

                Text(
                    text = address.toString(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}