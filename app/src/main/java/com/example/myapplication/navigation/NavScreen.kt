package com.example.myapplication.navigation

sealed class NavScreen(val route: String) {
    object Login : NavScreen("login")
    object StudentDashboard : NavScreen("student_dashboard")
    object Tasks : NavScreen("tasks")
    object StudentProfile : NavScreen("student_profile")
    object TaskDetail : NavScreen("task_detail/{taskId}") {
        fun createRoute(taskId: String): String {
            return "task_detail/$taskId"
        }
    }
    object LecturerDashboard : NavScreen("lecturer_dashboard")
    object LecturerTasks : NavScreen("lecturer_tasks")
    object CreateTask : NavScreen("create_task")
    object TaskProgress : NavScreen("task_progress/{taskId}") {

        fun createRoute(taskId: String): String {
            return "task_progress/$taskId"
        }
    }
    object LecturerProfile : NavScreen("lecturer_profile")

    object SignUp : NavScreen("sign_up")
}
