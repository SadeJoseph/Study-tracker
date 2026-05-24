package com.example.myapplication.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.myapplication.navigation.NavScreen

@Composable
fun LecturerBottomNavBar(
    navController: NavController
) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavScreen.LecturerDashboard.route)
            },
            icon = {
                Icon(Icons.Default.Home, contentDescription = "Dashboard")
            },
            label = {
                Text("Dashboard")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavScreen.LecturerTasks.route)
            },
            icon = {
                Icon(Icons.Default.List, contentDescription = "Tasks")
            },
            label = {
                Text("Tasks")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(NavScreen.LecturerProfile.route)
            },
            icon = {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            },
            label = {
                Text("Profile")
            }
        )
    }
}