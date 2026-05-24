package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.navigation.NavScreen
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.presentation.components.LecturerBottomNavBar
import com.example.myapplication.presentation.components.LecturerRecentTaskCard
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
@Composable
fun LecturerTaskListScreen(
    navController: NavController,
    vm: LecturerTaskListViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadTasks()
    }
    Scaffold(
        bottomBar = {
            LecturerBottomNavBar(navController)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Task Management",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "View and manage all module tasks",
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text = "Create New Task",
                clickButton = {
                    navController.navigate(NavScreen.CreateTask.route)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))
            // for empty and loading state
            if (vm.isLoading) {
                CircularProgressIndicator()
            } else if (vm.tasks.isEmpty()) {
                Text(
                    text = "No tasks available",
                    color = TextSecondary
                )

            } else {
                vm.tasks.forEach { task ->

                    LecturerRecentTaskCard(
                        title = task.title,
                        module = task.moduleCode,
                        progress = vm.taskProgressText[task.id] ?: "0/0 completed",
                        onClick = {
                            navController.navigate(
                                NavScreen.TaskProgress.createRoute(task.id)
                            )
                        },
                        onDelete = {
                            vm.deleteTask(task.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }
    }
}
