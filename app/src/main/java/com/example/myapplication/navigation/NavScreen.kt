package com.example.myapplication.navigation

sealed class NavScreen(val route: String) {
    object Login : NavScreen("login")
    object StudentDashboard : NavScreen("student_dashboard")
    object Tasks : NavScreen("tasks")
    object Profile : NavScreen("profile")
}
