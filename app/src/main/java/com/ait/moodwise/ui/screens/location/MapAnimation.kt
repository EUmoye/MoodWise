package com.ait.moodwise.ui.screens.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.util.Locale

@ExperimentalPermissionsApi
@Composable
fun RequestPermission(
    permission: String,
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
    onPermissionGranted: @Composable () -> Unit
) {
    val permissionState = rememberPermissionState(permission)
    val context = LocalContext.current
    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale
            ) { permissionState.launchPermissionRequest() }
        },
        content = {
            onPermissionGranted()
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun LocationContent(
    context: Context,
    navController: NavController,
    viewModel: MapViewModel
) {
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val location by viewModel.locationState

    LaunchedEffect(Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
            loc?.let {
                viewModel.locationState.value = it
                navController.navigate("Weather")
            }
        }
    }
    if (location == null) {
        WeatherAnimation()
    }
}

@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {

    if (shouldShowRationale) {

        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission Request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(rationaleMessage)
            },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text("Give Permission")
                }
            }
        )

    }
    else {
        Content(onClick = onRequestPermission)
    }

}

@Composable
fun Content(showButton: Boolean = true, onClick: () -> Unit) {
    if (showButton) {
        val enableLocation = remember { mutableStateOf(true) }
        if (enableLocation.value) {
            CustomDialogLocation(
                title = "Turn On Location Service",
                desc = "Explore the world without getting lost and keep the track of your location.\n\nGive this app a permission to proceed. If it doesn't work, then you'll have to do it manually from the settings.",
                enableLocation,
                onClick
            )
        }
    }
}

@Composable
fun WeatherAnimation() {
    val animations = listOf(
        "rain_animation.json",
        "sun_animation.json",
        "snow_animation.json",
        "cloud_animation.json"
    )
    var currentAnimationIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Change animation every 3 seconds
            currentAnimationIndex = (currentAnimationIndex + 1) % animations.size
        }
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animations[currentAnimationIndex]))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition,
        progress,
        modifier = Modifier.fillMaxSize()
    )
}
