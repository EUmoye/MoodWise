package com.ait.moodwise

//import com.ait.moodwise.ui.screens.WeatherDetails
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.ait.moodwise.ui.navigation.BottomNavItem
import com.ait.moodwise.ui.navigation.MainNavigation
import com.ait.moodwise.ui.theme.MoodWiseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodWiseTheme {
                val navController = rememberNavController()

                val items = listOf(
                    BottomNavItem(
                        title = "Weather",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Sharp.Home,
                        hasNews = false
                    ),
                    BottomNavItem(
                        title = "Music",
                        selectedIcon = Icons.Filled.PlayArrow,
                        unselectedIcon = Icons.Sharp.PlayArrow,
                        hasNews = false
                    )
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                Scaffold  (
                    bottomBar =  {
                        NavigationBar(
                            containerColor = Color.Transparent,
                        ) {

                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        navController.navigate(item.title)
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = Color.White,
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                            ))
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title,
                                            tint = Color.White
                                        )
                                    }
                                )
                            }
                        }
                    }) {
//                    innerPadding ->
                    MainNavigation(navController = navController)
                }
            }
        }
    }
}