package com.ait.moodwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ait.moodwise.ui.navigation.MainNavigation
import com.ait.moodwise.ui.theme.MoodWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MoodWiseTheme {
                MainNavigation()
            }
        }
    }
}