package com.ait.moodwise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.moodwise.ui.navigation.BottomNavItem
import com.ait.moodwise.ui.navigation.MainNavigation
import com.ait.moodwise.ui.theme.MoodWiseTheme
import io.github.cdimascio.dotenv.Dotenv
import com.ait.moodwise.ui.screens.MoodTracker
import com.ait.moodwise.ui.screens.WeatherDetails
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoodWiseTheme {
                val navController = rememberNavController()

                val items = listOf(
                    BottomNavItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = false
                    ),
                    BottomNavItem(
                        title = "Weather",
                        selectedIcon = Icons.Filled.LocationOn,
                        unselectedIcon = Icons.Outlined.LocationOn,
                        hasNews = false
                    )
                )
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
//                MainNavigation()
                Scaffold  (
                    bottomBar =  {
                        NavigationBar {
                          items.forEachIndexed { index, item ->
                              NavigationBarItem(
                                  selected = selectedItemIndex == index,
                                  onClick = {
                                      selectedItemIndex = index
                                      navController.navigate(item.title)
                                  },
                                  label = {
                                      Text(text = item.title)
                                  },
                                  icon = {
//                                      BadgedBox(
//                                          badge = {
//
//                                          }
//                                      )
//                                      {
                                          Icon(
                                              imageVector = if(index == selectedItemIndex) {
                                                  item.selectedIcon
                                              } else item.unselectedIcon,
                                              contentDescription = item.title
                                          )
//                                      }
                                  }
                              )
                          }
                        }
                }) {
                    innerPadding ->
                    MainNavigation(navController = navController)

                }
            }
        }
    }
}