package com.ait.moodwise.ui.screens.weather

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SplashScreen(
    navController: NavController
) {
    var animationState by remember { mutableStateOf(0) }
    val animations = listOf(
        "rain_animation.json",
        "sun_animation.json",
        "sun.json",
        "cloud_smiling.json"
    )

    LaunchedEffect(key1 = true) {
        while (true) {
            animationState = animationState % animations.size
            delay(3000) // Change animation every 3 seconds
            animationState++
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedFontSize by infiniteTransition.animateFloat(
        initialValue = 24f,
        targetValue = 32f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, bottom = 50.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Under the Weather",
                fontSize = animatedFontSize.sp,
                style = MaterialTheme.typography.displayMedium
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val compositionResult = rememberLottieComposition(
                spec = LottieCompositionSpec.Asset(animations[animationState])
            )

            val progress by animateLottieCompositionAsState(
                compositionResult.value,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = compositionResult.value,
                progress = { progress },
                modifier = Modifier
                    .size(200.dp)
            )
        }
        val context = LocalContext.current
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                navController.navigate("weather")
            } else {
                navController.navigate("map")
            }
        }

        LaunchedEffect(key1 = Unit) {
            delay(5000) // Show splash screen for 5 seconds
            // Navigate to your main screen here. Example:
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navController.navigate("map")
                }
                else -> {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }
}