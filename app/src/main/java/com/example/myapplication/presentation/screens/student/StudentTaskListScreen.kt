package com.example.myapplication.presentation.screens.student

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
import com.example.myapplication.presentation.components.BottomNavBar
import com.example.myapplication.presentation.components.TaskListCard
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.navigation.NavScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
@Composable
fun StudentTaskListScreen(
    navController: NavController,
    vm: StudentTaskListViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadTasks()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
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
                text = "My Tasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "View and manage your assignments",
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))
            if (vm.isLoading) {
                CircularProgressIndicator()
            } else if (vm.tasks.isEmpty()) {
                Text(
                    text = "No tasks available",
                    color = TextSecondary
                )

            } else {
                vm.tasks.forEach { task ->

                    TaskListCard(
                        title = task.title,
                        module = task.moduleCode,
                        dueDate = task.deadline,
                        status = vm.taskStatuses[task.id] ?: "NOT_STARTED",
                        onClick = {
                            navController.navigate(NavScreen.TaskDetail.createRoute(task.id))
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}