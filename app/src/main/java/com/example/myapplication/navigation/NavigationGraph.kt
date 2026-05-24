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
import com.example.myapplication.presentation.screens.tasks.TaskDetailScreen
import com.example.myapplication.presentation.screens.lecturer.LecturerDashboardScreen
import com.example.myapplication.presentation.screens.lecturer.CreateTaskScreen
import com.example.myapplication.presentation.screens.lecturer.LecturerProfileScreen
import com.example.myapplication.presentation.screens.lecturer.LecturerTaskListScreen
import com.example.myapplication.presentation.screens.lecturer.TaskProgressScreen
import com.example.myapplication.presentation.screens.signup.SignUpScreen
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
                },
                navigateToLecturerDashboard = {
                    navController.navigate(NavScreen.LecturerDashboard.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(NavScreen.SignUp.route)
                }
            )
        }

        composable(NavScreen.StudentDashboard.route) {
            StudentDashboardScreen(navController)
        }

        composable(NavScreen.Tasks.route) {
            StudentTaskListScreen(navController)
        }

        composable(NavScreen.StudentProfile.route) {
            StudentProfileScreen(navController)
        }

        composable(NavScreen.TaskDetail.route) { backStackEntry ->

            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""

            TaskDetailScreen(
                navController = navController,
                taskId = taskId
            )
        }
        composable(NavScreen.LecturerDashboard.route) {
            LecturerDashboardScreen(navController)
        }

        composable(NavScreen.LecturerTasks.route) {
            LecturerTaskListScreen(navController)
        }

        composable(NavScreen.CreateTask.route) {
            CreateTaskScreen(navController)
        }

        composable(NavScreen.TaskProgress.route) { backStackEntry ->
            val taskId =
                backStackEntry.arguments?.getString("taskId") ?: ""
            TaskProgressScreen(
                navController = navController,
                taskId = taskId
            )
        }
        composable(NavScreen.LecturerProfile.route) {
            LecturerProfileScreen(navController)
        }

        composable(NavScreen.SignUp.route) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}