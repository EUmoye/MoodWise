package com.ait.moodwise.ui.screens

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.Random
import com.google.accompanist.permissions.rememberPermissionState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.ait.moodwise.R
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherDetails(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isTrafficEnabled = true,
//                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
//                    context, R.raw.mymapconfig
//                ),
//                isMyLocationEnabled = true
            )
        )
    }
    Column {
        val fineLocationPermissionState = rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (fineLocationPermissionState.status.isGranted) {
            Column {

                Button(onClick = {
//                    mapViewModel.startLocationMonitoring()
                }) {
                    Text(text = "Start location monitoring")
                }
                Text(
                    text = "Location: "
//                            "${getLocationText(mapViewModel.locationState.value)}"
                )
            }

        } else {
            Column() {
                val permissionText = if (fineLocationPermissionState.status.shouldShowRationale) {
                    "Please consider giving permission"
                }
            else {
                    "Give permission for location"
                }
                Text(text = permissionText)
                Button(onClick = {
                    fineLocationPermissionState.launchPermissionRequest()
                }) {
                    Text(text = "Request permission")
                }
            }
        }


        var isSatellite by remember { mutableStateOf(false) }
        Switch(checked = isSatellite,
            onCheckedChange = {
                isSatellite = it
                mapProperties = mapProperties.copy(
                    mapType = if (isSatellite) MapType.SATELLITE else MapType.NORMAL
                )
            })

//        Text(text = geocodeText)


    }
}