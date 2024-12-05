package com.ait.moodwise.ui.navigation

import androidx.compose.runtime.Composable
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