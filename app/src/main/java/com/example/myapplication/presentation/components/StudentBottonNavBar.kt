package com.example.myapplication.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import com.example.myapplication.navigation.NavScreen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, NavScreen.StudentDashboard.route),
        BottomNavItem("Tasks", Icons.Default.List, NavScreen.Tasks.route),
        BottomNavItem("Profile", Icons.Default.Person, NavScreen.StudentProfile.route)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = false,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}