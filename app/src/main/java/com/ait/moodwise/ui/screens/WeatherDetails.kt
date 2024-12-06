package com.ait.moodwise.ui.screens

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ait.moodwise.R
import com.ait.moodwise.data.activity.ActivitiesItem
import com.ait.moodwise.data.activity.Activity
import com.ait.moodwise.data.weather.weatherInfo
import com.ait.moodwise.ui.screens.location.MapViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlin.math.roundToInt

@Composable
fun WeatherAnimation() {
    val animations = listOf(
        "rain_animation.json",
        "sun.json",
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
@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherDetails(mapViewModel: MapViewModel) {
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }

    val viewModel: WeatherDetailsViewModel = hiltViewModel()
    val weatherInfo = (viewModel.weatherUIState as? WeatherUIState.Success)?.weatherInfo
    val genAIViewModel: GenAIViewModel = hiltViewModel()
    val location by mapViewModel.locationState
    val context = LocalContext.current
    var cityCountry by rememberSaveable { mutableStateOf("New York") }
    val activitiesList by genAIViewModel.activitiesList.collectAsState()
    var initialTimeInMillis by rememberSaveable { mutableStateOf(0L) }
    var localTime by rememberSaveable { mutableStateOf("") }
    var bcgImg by rememberSaveable { mutableStateOf(R.drawable.weather_clear) }

    LaunchedEffect(location) {
        location?.let {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            cityCountry = addresses?.firstOrNull()?.let { address ->
                "${address.locality},${address.countryCode}"
            } ?: "New York,US" // Default value if location is not available

            viewModel.getWeatherDetails(
                cityCountry = cityCountry,
                apiKey = "3b3fb7a3dda7a0a2788ae82328224214"
            )

        }
    }

    LaunchedEffect(weatherInfo) {
//        if (weatherInfo == null) {
//            viewModel.getWeatherDetails(
//                cityCountry = "Budapest",
//                apiKey = "3b3fb7a3dda7a0a2788ae82328224214"
//            )
//        }
//
//        else {
//            bcgImg = getWeatherBackground(weatherInfo.weather[0].main.lowercase())
//            localTime = viewModel.getLocalTime(weatherInfo.dt, weatherInfo.timezone)
//        }
        weatherInfo?.let {
            bcgImg = getWeatherBackground(it.weather[0].main.lowercase())
            localTime = viewModel.getLocalTime(it.dt, it.timezone)
        }
    }

    LaunchedEffect(cityCountry) {
        genAIViewModel.fetchActivities(cityCountry)
    }

    LaunchedEffect(Unit) {
        viewModel.startPeriodicUpdates(cityCountry, "3b3fb7a3dda7a0a2788ae82328224214")
    }

    Box(
            modifier =  Modifier.fillMaxSize()
    ) {

        AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bcgImg)
                    .crossfade(true)
                    .build(),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
                    .blur(radiusX = 15.dp, radiusY = 15.dp)
            )

            when (viewModel.weatherUIState) {
                is WeatherUIState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator() // Update this to have the animation of different weathers
                        WeatherAnimation()
                    }
                }

                is WeatherUIState.Success -> {
                    if (weatherInfo != null) {
                        WeatherDetailsContent(
                            localTime = localTime,
                            weatherInfo = weatherInfo,
                            cityCountry = cityCountry,
                            currentTime = currentTime.value,
                            activities = activitiesList,
                            viewModel = viewModel,
                            onCityCountryChange = { newCityContry ->
                                cityCountry = newCityContry }
                        )
                    }
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
    localTime: String,
    weatherInfo: weatherInfo,
    cityCountry: String,
    currentTime: Long,
    activities: List<ActivitiesItem>,
    viewModel: WeatherDetailsViewModel,
    onCityCountryChange: (String) -> Unit

) {
    var isDetailsDialog by rememberSaveable { mutableStateOf(false) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedActivity by remember { mutableStateOf<ActivitiesItem?>(null) }
    val scrollState = rememberScrollState()
    var showSearchBar by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp, start = 20.dp, end = 20.dp)
            .verticalScroll(scrollState)
    ) {
        if (showSearchBar) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = { searchQuery = it},
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        onCityCountryChange(searchQuery)
                        viewModel.getWeatherDetails(
                            cityCountry = searchQuery,
                            apiKey = "3b3fb7a3dda7a0a2788ae82328224214"
                        )
                    }
                } ,
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
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                IconButton(
                    onClick = {
                        showSearchBar = true
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                )) {
                    if (!showSearchBar) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            }

            Text(
                text = if (localTime == "") "" else "Local Time: $localTime",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }

        // Weather Info Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            // Temperature
            Column {
                Text(
                    text = "${weatherInfo.main.temp.roundToInt()}°C",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "Feels like ${weatherInfo.main.feelsLike.roundToInt()}°C",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium,
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
//                .background(Color.Transparent),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val width = ((LocalConfiguration.current.screenWidthDp.dp)/2)
            val totalHeight = 200.dp // Example fixed height, adjust as needed

            Card(
                modifier = Modifier
                    .width(width)
                    .height(totalHeight)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .border(
                        border = BorderStroke(1.dp, color = Color.White),
                        shape = RoundedCornerShape(10.dp)

                    ),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.padding(totalHeight*0.12f)) {
                    Column (
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .background(Color.Transparent),
                    ) {
                        Text(
                            text = "${weatherInfo.name.uppercase()}",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White
                        )
                        Text(
                            text = "Our report for ${weatherInfo.name} today is ${
                                weatherInfo.weather.get(
                                    0
                                ).description
                            }." +
                                    "We hope you enjoy your day!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }

                }
            }

            // then create a column for the three top items: hum, wind, and, main
            Column(
                modifier = Modifier
                    .width(width)
                    .height(totalHeight)
                    .padding(bottom = 16.dp)
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier
                        .width(width)
                        .padding(top = 16.dp)
                        .height(totalHeight/3)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .border(
                            border = BorderStroke(1.dp, color = Color.White),
                            shape = RoundedCornerShape(10.dp)

                        )
                        .align(Alignment.CenterHorizontally),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(modifier = Modifier
                        .padding(8.dp)
//                        .height()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "${weatherInfo.weather.get(0).main.uppercase()}",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "${weatherInfo.weather.get(0).description}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .width(width)
                        .height(totalHeight/2)
                        .background(Color.Transparent),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),

                ) {
                    val width = ((LocalConfiguration.current.screenWidthDp.dp)/2)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Black.copy(alpha = 0.3f))
                            .border(
                                border = BorderStroke(1.dp, color = Color.White),
                                shape = RoundedCornerShape(10.dp)

                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                            .background(Color.Transparent)
                            ) {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 24.dp)
                                    .background(Color.Transparent),
                                verticalArrangement = Arrangement.spacedBy(18.dp)
                            ) {
                                Text(
                                    text = "HUMIDITY",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )

                                Text(
                                    text = "${weatherInfo.main.humidity}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            }

                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Black.copy(alpha = 0.3f))
                            .border(
                                border = BorderStroke(1.dp, color = Color.White),
                                shape = RoundedCornerShape(10.dp)

                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(bottom = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(18.dp)
                            ) {
                                Text(
                                    text = "WIND",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                )

                                Text(
                                    text = "${weatherInfo.wind.speed}m/s",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    }
                }

            }


        }

        // Recommended Activities
        Column (
            modifier = Modifier
                .padding(bottom = 20.dp, start = 16.dp, end = 16.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
            Text(
                text = "RECOMMENDED ACTIVITIES",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                textAlign = TextAlign.Center

            )
            if (!activities.isEmpty()){
                var width = (LocalConfiguration.current.screenWidthDp.dp)
                Column(
                    modifier = Modifier
                        .width(width)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                        ) {
                            for (j in 0 until 2) {
                                val index = i * 2 + j
                                if (index < activities.size) {
                                    ActivityCard(
                                        activity = activities[index],
                                        onClick = {
                                            selectedActivity = activities[index]
                                            isDetailsDialog = true
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }

//            else {
//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 8.dp)
//                        .background(Color.Transparent),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    items(recommendedActivities.size) {
//                            index ->  ActivityCard(
//                        recommendedActivities[index],
//                        onClick = {
//                            isDetailsDialog = true
//                        }
//                    )
//                    }
//                }
//            }
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

    if (isVisible) {
        Dialog(onDismissRequest = onDismissRequest) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f) // Set the height to half the screen height
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close"
                            )
                        }
                    }
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
    var width = (LocalConfiguration.current.screenWidthDp.dp) / 2
    Card(
        modifier = Modifier
            .width(width)
            .height(120.dp)
            .background(Color.Transparent)
            .clickable(onClick = onClick)
            .padding(8.dp)
            .border(
            border = BorderStroke(1.dp, color = Color.White),
                shape = RoundedCornerShape(10.dp)
            ),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        ) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.White

            )
        }
    }
}

@Composable
fun WeatherInfo(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

fun getWeatherBackground(weather: String): Int {
    return when (weather) {
        "clear" -> R.drawable.weather_clear
        "cloudy" -> R.drawable.weather_cloud_4
        "clouds", "few cloud", "partial cloud" -> R.drawable.weather_cloud_4
        "rain", "rainy" -> R.drawable.weather_rain
        "snow" -> R.drawable.weather_snow
        "thunderstorm" -> R.drawable.weather_lightning_1
        "drizzle" -> R.drawable.weather_drizzle
        "fog", "mist" -> R.drawable.weather_fog
        "haze" -> R.drawable.weather_fog
        "dust", "sand" -> R.drawable.weather_dust
        "tornado" -> R.drawable.weather_tornado
        "windy", "wind" -> R.drawable.weather_windy
        "sunny", "sun" -> R.drawable.weather_sunny
        "hurricane" -> R.drawable.weather_hurricane
        "tornado" -> R.drawable.weather_tornado
        "storm" -> R.drawable.weather_lightning_1
        else -> R.drawable.weather_clear

    }
}





