package com.application.weatherapp.view.ui

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.application.weatherapp.R
import com.application.weatherapp.android.service.location.LocationService
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.service.MetNorwayApi
import com.application.weatherapp.network.api.service.NominatimApi
import com.application.weatherapp.view.ui.animation.PopupComponentAnimation
import com.application.weatherapp.view.ui.button.CircleButton
import com.application.weatherapp.view.ui.text.EmptyTextToolbar
import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

@OptIn(ExperimentalPermissionsApi::class)
private lateinit var _locationPermission: MultiplePermissionsState
private lateinit var _weatherViewModel: WeatherViewModel
private lateinit var _locationViewModel: LocationViewModel

private lateinit var _focusManager: FocusManager
private lateinit var _isSearching: MutableState<Boolean>

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun LocationSearchBar(
    modifier: Modifier,
    locationViewModel: LocationViewModel,
    weatherViewModel: WeatherViewModel
) {
    _locationPermission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    _locationViewModel = locationViewModel
    _weatherViewModel = weatherViewModel
    _focusManager = LocalFocusManager.current

    val lifecycle = LocalLifecycleOwner.current
    val density = LocalDensity.current

    var textAddress by remember { mutableStateOf("") }

    var hasFocus by remember { mutableStateOf(false) }

    var startY by remember { mutableStateOf(0) }
    var startX by remember { mutableStateOf(0) }

    val focusRequester = remember { FocusRequester() }

    _isSearching = remember { mutableStateOf(false) }

    val locations = locationViewModel.locations.observeAsState()

    locationViewModel.currentLocation.observe(lifecycle) {
        if (!hasFocus)
            textAddress = it.toString()

        _isSearching.value = false
    }

    locationViewModel.locations.observe(lifecycle) {
        _isSearching.value = false
    }

    val shape = RoundedCornerShape(12.dp)

    CompositionLocalProvider(
        LocalTextToolbar provides EmptyTextToolbar()
    ) {
        TextField(
            value = textAddress,
            onValueChange = {
                textAddress = it

                _isSearching.value = true
                locationViewModel.getLocations(textAddress, 3, NominatimApi)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { _focusManager.clearFocus() }
            ),
            modifier = modifier
                .onFocusChanged { focusState ->
                    hasFocus = focusState.hasFocus
                }
                .focusRequester(focusRequester)
                .focusable()
                .onGloballyPositioned { textField ->
                    startY = with(density) {
                        (textField.positionInParent().y + textField.size.height)
                            .toDp()
                            .roundToPx()
                    }
                    startX = with(density) {
                        textField.positionInParent().x
                            .toDp()
                            .roundToPx()
                    }
                },
            trailingIcon = {
                LocationButton(modifier = Modifier.padding(end = 4.dp))
            },
            leadingIcon = {
                MenuButton(modifier = Modifier.padding(start = 4.dp))
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
            shape = shape
        )

        UploadingPopup(
            offset = IntOffset(startX, startY),
            shape = shape,
            visible = _isSearching.value,
            modifier = Modifier
                .padding(top = 4.dp, start = 16.dp, end = 16.dp)
        )

        QueryResultPopup(
            offset = IntOffset(startX, startY),
            visible = hasFocus,
            shape = shape,
            locations = locations.value!!,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = shape,
                    clip = true,
                    ambientColor = Color.Gray,
                    spotColor = Color.Gray
                )
        )
    }

}

@Composable
private fun MenuButton(
    modifier: Modifier = Modifier
) {
    CircleButton(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = MaterialTheme.colorScheme.onTertiary,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationButton(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    CircleButton(
        onClick = {
            scope.launch {
                if (!_locationPermission.allPermissionsGranted)
                    _locationPermission.launchMultiplePermissionRequest()
                else {
                    _isSearching.value = true

                    val currentLocation =
                        LocationService.getCurrentLocation(context, NominatimApi)

                    _locationViewModel.setCurrentLocation(currentLocation)
                    _weatherViewModel.downloadWeatherData(currentLocation, MetNorwayApi)
                }
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
private fun QueryResultPopup(
    modifier: Modifier = Modifier,
    offset: IntOffset,
    visible: Boolean,
    shape: Shape,
    locations: List<Location>
) {
    Popup(
        alignment = Alignment.TopStart,
        offset = offset
    ) {
        PopupComponentAnimation(
            visible = visible,
            durationMills = 300,
            startHeight = 0.dp
        ) {
            Surface(
                modifier = modifier,
                color = Color.DarkGray,
                shape = shape
            ) {
                Column {
                    for (i in locations.indices) {
                        LocationText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    Timer().schedule(
                                        timerTask {
                                            _weatherViewModel.downloadWeatherData(
                                                locations[i],
                                                MetNorwayApi
                                            )
                                        }, 500
                                    )

                                    _locationViewModel.setCurrentLocation(locations[i])
                                    _focusManager.clearFocus()
                                }
                                .padding(8.dp),

                            location = locations[i]
                        )

                        if (i != locations.lastIndex) {
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

@Composable
private fun LocationText(
    location: Location,
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

    Row(modifier = modifier) {
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

@Composable
private fun UploadingPopup(
    modifier: Modifier = Modifier,
    visible: Boolean,
    offset: IntOffset,
    shape: Shape
) {
    var maxWidth by remember { mutableStateOf(0.dp) }

    val insideBarWidth = 80.dp
    val barHeight = 4.dp

    val density = LocalDensity.current

    val targetValue = maxWidth - insideBarWidth

    val moveAnimation by rememberInfiniteTransition()
        .animateValue(
            initialValue = 0.dp,
            targetValue = targetValue,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )

    if (visible) {
        Popup(
            alignment = Alignment.TopStart,
            offset = offset
        ) {
            Box(modifier = modifier
                .onGloballyPositioned {
                    maxWidth = with(density) { it.size.width.toDp() }
                }
            ) {
                Surface(
                    shape = shape
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(barHeight)
                            .background(Color.Gray)
                    )
                }

                Surface(
                    modifier = Modifier.offset(x = moveAnimation),
                    shape = shape
                ) {
                    Spacer(
                        Modifier
                            .width(insideBarWidth)
                            .height(barHeight)
                            .background(Color.DarkGray)
                    )
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

