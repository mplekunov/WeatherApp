package com.application.weatherapp.view.ui.weather


import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.application.weatherapp.R
import com.application.weatherapp.network.api.service.MetNorwayApi
import com.application.weatherapp.network.api.service.NominatimApi

import com.application.weatherapp.viewmodel.LocationViewModel
import com.application.weatherapp.viewmodel.WeatherViewModel

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
            .focusRequester(focusRequester)
            .focusable()
            .onFocusChanged { focusState ->
                hasFocus = focusState.hasFocus
            }
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
        shape = RoundedCornerShape(4.dp)
    )

    if (hasFocus) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(textFieldX, textFieldY)
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(4.dp),
                        ambientColor = Color.Blue,
                        spotColor = Color.Red
                    ),
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(4.dp)
            ) {
                Column {
                    for (i in locations.value!!.indices) {
                        LocationText(
                            location = locations.value!![i],
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                                .fillMaxWidth()
                                .clickable {
                                    weatherViewModel.downloadWeatherData(locations.value!![i], MetNorwayApi)

                                    textAddress = locations.value!![i].toString()

                                    focusManager.clearFocus()
                                }
                        )

                        if (i != locations.value!!.lastIndex) {
                            Spacer(
                                modifier = Modifier
                                    .padding(top = 4.dp, bottom = 4.dp, start = 48.dp, end = 48.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onTertiary)
                            )
                        }
                    }
                }
            }
        }
    }
}
//
//@Composable
//private fun SearchResultPopup(
//    locations: List<com.application.weatherapp.model.Location>,
//    modifier: Modifier = Modifier,
//    offset: IntOffset,
//    weatherViewModel: WeatherViewModel
//) {
//
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