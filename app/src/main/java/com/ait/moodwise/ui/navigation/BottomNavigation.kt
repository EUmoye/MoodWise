package com.ait.moodwise.ui.navigation

//import androidx.compose.material3.BottomNavigationItem
import android.media.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.material.BottomNavigation


@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    onItemClick: (BottomNavItem) -> Unit
) {
//    BottomNavigation {
//        items.forEach { item ->
//            BottomNavigationItem(
//                icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
//                label = { Text(text = item.name) },
//                selected = item.isSelected,
//                onClick = { onItemClick(item) }
//            )
//        }
//    }
}

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
)