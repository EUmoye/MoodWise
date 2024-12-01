package com.ait.moodwise.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ait.moodwise.R
import com.ait.moodwise.data.activity.ActivitiesItem
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetails(viewModel: WeatherDetailsViewModel = hiltViewModel()) {
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        viewModel.getWeatherDetails(cityCountry = "New York,US", apiKey = "3b3fb7a3dda7a0a2788ae82328224214")
        viewModel.startPeriodicUpdates(cityCountry = "New York,US", apiKey = "3b3fb7a3dda7a0a2788ae82328224214")

        // Periodic time updates
        while (true) {
            currentTime.value = System.currentTimeMillis()
            kotlinx.coroutines.delay(60000) // Update every minute
        }
    }
Box(
    modifier =  Modifier.fillMaxSize()
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.weather_lightning_1)
            .crossfade(true)
            .build(),
        contentDescription = "Background Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
            .blur(radiusX = 15.dp, radiusY = 15.dp)
    )

//    // Semi-transparent overlay
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 1.5f))
//    )

    when (viewModel.weatherUIState) {
        is WeatherUIState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Text(text = "Loading Weather Details...")
            }
        }

        is WeatherUIState.Success -> {
            WeatherDetailsContent(
                weatherInfo = (viewModel.weatherUIState as WeatherUIState.Success).weatherInfo,
                currentTime = currentTime.value
            )
        }

        is WeatherUIState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Failed to load weather details. Please try again.")
            }
        }
    }
}
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WeatherDetailsContent(
    weatherInfo: weatherInfo,
//    weather: String?,
    currentTime: Long

) {
    var testModel: GenAIViewModel = hiltViewModel()
    var activitiesList by rememberSaveable { mutableStateOf<List<ActivitiesItem>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        activitiesList = testModel.json_no_schema()

    }
    var isDetailsDialog by rememberSaveable { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<ActivitiesItem?>(null) }
    var recommendedActivities = listOf(ActivitiesItem("sleep", "in budapest", "just sleep"))
    val scrollState = rememberScrollState()
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp)
            .verticalScroll(scrollState)

//            .padding(50.dp)
    ) {
        if (!activitiesList.isEmpty()) {
            Log.d("length of list", activitiesList.size.toString())
            Log.d("activities list", activitiesList.get(0).name)
        }

        if (showSearchBar) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it},
                onSearch = {} ,
                onCloseSearchBar = {
                    showSearchBar = false
                }
            )
        }
        // Top Section: City Name and Date/Time
        Column(modifier = Modifier

            .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
            .background(Color.Transparent),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Text(
                text = weatherInfo.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                IconButton(
                    onClick = {
                        showSearchBar = true
                    }
                ) {
                    if (!showSearchBar) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            }

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
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
//                .padding(bottom = 16.dp)
                .background(Color.Transparent),
//                .graphicsLayer {
//                    renderEffect = BlurEffect(10f, 10f)
//                },
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            // Temperature
            Column {
                Text(
                    text = "${weatherInfo.main.temp}°C",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Feels like ${weatherInfo.main.feelsLike}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Placeholder for weather icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Transparent)
            ) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data("https://openweathermap.org/img/w/${weatherInfo.weather?.get(0)?.icon}.png")
                    .crossfade(true)
                    .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                        .clip(CircleShape)
                )
            }
        }

        // City Description
        Card(
            modifier = Modifier
                .fillMaxWidth()
            .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
            .background(Color.Gray.copy(alpha = 0.2f))
                .border(
                    border = BorderStroke(1.dp, color = Color.Black),
                    shape = RoundedCornerShape(10.dp)

                ),
//                    Color.Gray.copy(alpha = 0.5f),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(modifier = Modifier.padding(16.dp)
                .background(Color.Transparent)
            ) {
                Text(
                    text = "CITY DESCRIPTION",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "Cloudiness: ${weatherInfo.clouds.all}%",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Wind Speed: ${weatherInfo.wind.speed}m/s",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }

        // Stats Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
            .padding(bottom = 20.dp, start = 16.dp, end = 16.dp),


        horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width((LocalConfiguration.current.screenWidthDp.dp)/2)
                    .height(80.dp)
                    .padding(8.dp)
                    .background(Color.Transparent)
                    .border(
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(10.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Humidity: ${weatherInfo.main.humidity}%",
                    style = MaterialTheme.typography.bodyMedium)
            }
            Box(
                modifier = Modifier
                    .width((LocalConfiguration.current.screenWidthDp.dp)/2)
                    .height(80.dp)
                    .background(Color.Transparent)
                    .border(
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
//                    text = "weather",
                    text ="Pressure: ${weatherInfo.main.pressure} hPa",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Recommended Activities
        Column (
            modifier = Modifier
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                .background(Color.Transparent)
            ) {
            Text(
                text = "RECOMMENDED ACTIVITIES",
                style = MaterialTheme.typography.labelMedium
            )
            if (!activitiesList.isEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    for (i in 0 until 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
//                                .padding(bottom = 16.dp),
//                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            for (j in 0 until 2) {
                                val index = i * 2 + j
                                if (index < activitiesList.size) {
                                    ActivityCard(
                                        activity = activitiesList[index],
                                        onClick = {
                                            selectedActivity = activitiesList[index]
                                            isDetailsDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(Color.Transparent),
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
        }
        
        if (isDetailsDialog && selectedActivity != null) {
            ShowActivitiesDetailsDialog(
                activity = selectedActivity!!,
                isVisible = isDialogVisible,
                onDismissRequest = {
                    isDialogVisible = false
                    isDetailsDialog = false
                                   },
            ){}
            LaunchedEffect(Unit) {
                isDialogVisible = true
            }
        }

    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    onCloseSearchBar: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    keyboardController?.show()
                }
            }
            .background(Color.Transparent)
//            .graphicsLayer {
//                renderEffect = BlurEffect(10f, 10f)
//            }
            .padding(16.dp),
        label = { Text("Search a city's weather and press enter") },
        trailingIcon = {
            IconButton(onClick = onSearch) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        },

        keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onSearch()
                onCloseSearchBar()
            }
        )
    )
}

@Composable
fun ShowActivitiesDetailsDialog(
    activity: ActivitiesItem,
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
        var offsetY by remember { mutableStateOf(0f) }
        var scrollState = rememberScrollState()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn((LocalConfiguration.current.screenHeightDp.dp) / 4 * 4)
                .offset { IntOffset(0, offsetY.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetY += dragAmount.y
                        if (offsetY < 0f) offsetY = 0f
                    }
                }.background(Color.Transparent),
//                .graphicsLayer {
//                    renderEffect = BlurEffect(10f, 10f)
//                },// Adjust height as needed
//                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ){
                Text(
                    text = "^",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
//                    .verticalScroll(scrollState)
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
                        text = activity.location,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = activity.description,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

    }

}

// Reusable Activity Card
@Composable
fun ActivityCard(
    activity: ActivitiesItem,
    onClick: () -> Unit
) {
    
    Card(
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp.dp)/2)
            .height(120.dp)
            .background(Color.Transparent)
            .clickable(onClick = onClick)
            .border(
            border = BorderStroke(1.dp, color = Color.Black),
                shape = RoundedCornerShape(10.dp)
            ),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Transparent)
        ) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}


