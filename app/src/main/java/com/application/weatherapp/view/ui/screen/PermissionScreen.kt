package com.application.weatherapp.view.ui.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.application.weatherapp.model.Location
import com.application.weatherapp.network.api.service.MetNorwayApi
import com.application.weatherapp.network.api.service.NominatimApi
import com.application.weatherapp.ui.theme.WeatherAppTheme
import com.application.weatherapp.view.ui.animation.PopupComponentAnimation
import com.application.weatherapp.view.ui.animation.WaitAnimation
import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.*
import kotlin.concurrent.timerTask


@OptIn(ExperimentalPermissionsApi::class)
private lateinit var _locationPermission: MultiplePermissionsState
private lateinit var _weatherViewModel: WeatherViewModel
private lateinit var _locationViewModel: LocationViewModel
private lateinit var _permissionPopup: MutableState<Boolean>

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    weatherViewModel: WeatherViewModel,
    locationViewModel: LocationViewModel
) {
    _locationPermission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    _locationViewModel = locationViewModel
    _weatherViewModel = weatherViewModel

    _permissionPopup = remember { mutableStateOf(false) }

    if (_locationPermission.allPermissionsGranted)
        PrepareHomeScreen()
    else {
        Surface(
            color = Color.Black,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                Spacer(modifier = Modifier.weight(0.7f))

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Gray)
                )
                Column(
                    modifier = Modifier
                        .background(Color.DarkGray)
                        .fillMaxWidth()
                        .weight(0.3f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "This application requires access to location services.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 18.dp)
                    )

                    Button(
                        onClick = { _permissionPopup.value = true },
                        modifier = Modifier
                            .padding(top = 36.dp, start = 8.dp, end = 8.dp)
                            .height(50.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                                    .padding(0.dp)
                                    .fillMaxHeight()
                            )

                            Text(
                                text = "REQUEST LOCATION ACCESS",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    LocationPermissionPopup(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationPermissionPopup(
    modifier: Modifier = Modifier
) {
    val maxHeight = LocalConfiguration.current.screenHeightDp.dp

    Popup(
        alignment = Alignment.TopStart
    ) {
        PopupComponentAnimation(
            visible = _permissionPopup.value,
            durationMills = 400,
            startHeight = maxHeight
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(8.dp),
                color = Color.Gray
            ) {
                Column {
                    Box {
                        Box(
                            modifier = Modifier
                                .background(Color.Blue)
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(60.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 24.dp, end = 24.dp)
                    ) {
                        Text(
                            text = "Location Permission",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(top = 18.dp)
                        )

                        Text(
                            text = "In order to get weather forecast for your current location, you have to grant WeatherApp permission to access your location!",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 24.dp, bottom = 48.dp)
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(start = 12.dp, end = 18.dp, top = 4.dp, bottom = 4.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .weight(.4f)
                        )

                        Button(
                            onClick = {
                                _permissionPopup.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }

                        Button(
                            onClick = {
                                _locationPermission.launchMultiplePermissionRequest()
                            },
                            modifier = Modifier
                                .weight(.3f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Continue"
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun PrepareHomeScreen() {
    var isHomeScreenReady by remember { mutableStateOf(false) }

    if (!isHomeScreenReady)
        WaitAnimation()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(null) {
        val currentLocation = LocationServices
            .getFusedLocationProviderClient(context)
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)

        currentLocation.addOnCompleteListener {
            val homeLocation = Location(it.result.latitude, it.result.longitude)

            _locationViewModel.searchForLocation(
                homeLocation.latitude,
                homeLocation.longitude,
                NominatimApi
            )

            _locationViewModel.locations.observe(lifecycleOwner) {
                _weatherViewModel.downloadWeatherData(homeLocation, MetNorwayApi)
                _locationViewModel.setCurrentLocation(homeLocation)
            }

            _weatherViewModel.currentWeather.observe(lifecycleOwner) {
                Timer().schedule(timerTask {
                    isHomeScreenReady = true

                    (context as ComponentActivity).setContent {
                        WeatherAppTheme {
                            HomeScreen(
                                _weatherViewModel,
                                _locationViewModel
                            )
                        }
                    }
                }, 5000)
            }
        }
    }
}