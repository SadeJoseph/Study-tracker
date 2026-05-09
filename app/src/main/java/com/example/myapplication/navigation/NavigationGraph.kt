package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.screens.login.LoginScreen
import com.example.myapplication.presentation.screens.student.StudentDashboardScreen
import com.example.myapplication.presentation.screens.student.StudentTaskListScreen
import com.example.myapplication.presentation.screens.student.StudentProfileScreen



@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavScreen.Login.route,
        modifier = modifier
    ) {
        composable(NavScreen.Login.route) {
            LoginScreen(
                navigateToStudentDashboard = {
                    navController.navigate(NavScreen.StudentDashboard.route)
                }
            )
        }

        composable(NavScreen.StudentDashboard.route) {
            StudentDashboardScreen(navController)
        }

        composable(NavScreen.Tasks.route) {
            StudentTaskListScreen(navController)
        }

        composable(NavScreen.Profile.route) {
            StudentProfileScreen(navController)
        }
    }
}