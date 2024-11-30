package com.ait.moodwise.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import com.ait.moodwise.data.activity.Activity
import com.ait.moodwise.data.weather.weatherInfo
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.GoogleMap
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.maps.android.compose.rememberCameraPositionState
import com.ait.moodwise.ui.screens.MapViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Timer
import java.util.TimerTask

//@Composable
//fun WeatherDetails(viewModel: WeatherDetailsViewModel = hiltViewModel()) {
//    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }
//
//    LaunchedEffect(Unit) {
//        viewModel.getWeatherDetails(cityCountry = "New York,US", apiKey = "3b3fb7a3dda7a0a2788ae82328224214")
//        viewModel.startPeriodicUpdates(cityCountry = "New York,US", apiKey = "3b3fb7a3dda7a0a2788ae82328224214")
//
//        // Periodic time updates
//        while (true) {
//            currentTime.value = System.currentTimeMillis()
//            kotlinx.coroutines.delay(60000) // Update every minute
//        }
//    }
//
//    when (viewModel.weatherUIState) {
//        is WeatherUIState.Loading -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//                Text(text = "Loading Weather Details...")
//            }
//        }
//        is WeatherUIState.Success -> {
//            WeatherDetailsContent(
//                weatherInfo = (viewModel.weatherUIState as WeatherUIState.Success).weatherInfo,
//                recommendedActivities = listOf("Swimming", "Beach Volleyball", "Hiking"),
//                currentTime = currentTime.value
//            )
//        }
//        is WeatherUIState.Error -> {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = "Failed to load weather details. Please try again.")
//            }
//        }
//    }
//}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WeatherDetailsContent(
    weatherInfo: weatherInfo? = null,
    weather: String,
    recommendedActivities: List<String>,
    currentTime: Long

) {
    var testModel: GenAIViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        testModel.json_no_schema()
    }
    var isDetailsDialog by rememberSaveable { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
//            .padding(50.dp)
    ) {
        // Top Section: City Name and Date/Time
        Column(modifier = Modifier

            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "weather",
//                text = weatherInfo.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Local Time: ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(currentTime))}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Weather Info Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
//                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Temperature
            Column {
                Text(
                    text = weather,
//                    text = "${weatherInfo.main.temp}°C",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "weather",
//                    text = "Feels like ${weatherInfo.main.feelsLike}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            // Placeholder for weather icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.LightGray, shape = CircleShape)
            )
        }

        // City Description
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(bottom = 16.dp),
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(8.dp),
//            backgroundColor = Color(0xFFF5F5DC)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "CITY DESCRIPTION",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = "weather",
//                    text = "Cloudiness: ${weatherInfo.clouds.all}%",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "weather",
//                    text = "Wind Speed: ${weatherInfo.wind.speed}m/s",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Stats Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(bottom = 16.dp),
            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),

        horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF5F5DC), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "weather",
//                    text = "Humidity: ${weatherInfo.main.humidity}%",
                    style = MaterialTheme.typography.bodySmall)
            }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color(0xFFF5F5DC), shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "weather",
//                    text ="Pressure: ${weatherInfo.main.pressure} hPa",
                    style = MaterialTheme.typography.bodySmall)
            }
        }

        // Recommended Activities
        Column (
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            ) {
            Text(
                text = "RECOMMENDED ACTIVITIES",
                style = MaterialTheme.typography.labelMedium
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items(recommendedActivities.size) {
                    index ->  ActivityCard(
                    recommendedActivities[index],
                        onClick = {
                            isDetailsDialog = true
                        }
                    )
                }
            }
        }
        
        if (isDetailsDialog) {
            ShowActivitiesDetailsDialog(
                isVisible = isDialogVisible,
                onDismissRequest = {
                    isDialogVisible = false
                    isDetailsDialog = false
                                   },
            ){}
            LaunchedEffect(Unit) {
                isDialogVisible = true
            }
//            LaunchedEffect(Unit) {
//                delay(300)
//                isDetailsDialog = false
//            }

        }

    }
}

@Composable
fun ShowActivitiesDetailsDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    properties: Activity?= null,
    content: () -> Unit) {

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it }, // small slide 300px
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing // interpolator
            )
        ),
        exit = slideOutVertically (
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        )
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Adjust height as needed
//                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
//                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = {
                        onDismissRequest()

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close"
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Activity Details",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "Activity Name",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Activity Location"
                    )
                }
            }
        }

    }

}

// Reusable Activity Card
@Composable
fun ActivityCard(
    activityName: String,
    onClick: () -> Unit
) {
    
    Card(
        modifier = Modifier
//            .fillMaxSize()
            .size(120.dp, 80.dp)
//            .align(Alignment.BottomCenter)
//            .align(Alignment.BottomCenter) // Aligns it at the bottom
            .clickable(onClick = onClick), // Adjust size as needed
        shape = RoundedCornerShape(8.dp),

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = activityName,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


