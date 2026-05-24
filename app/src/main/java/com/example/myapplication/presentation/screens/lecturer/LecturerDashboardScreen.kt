package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.myapplication.ui.theme.CardWhite
import com.example.myapplication.ui.theme.LightBlue
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.presentation.components.LecturerRecentTaskCard
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun LecturerDashboardScreen(
    navController: NavController,
    vm: LecturerDashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
    vm.loadDashboard()
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
                .padding(20.dp)
        ) {
            Text(
                text = "Lecturer Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Manage your module tasks and track student progress",
                fontSize = 15.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LecturerSummaryCard(
                    value = vm.totalTasks.toString(),
                    label = "Total Tasks",
                    modifier = Modifier.weight(1f)
                )

                LecturerSummaryCard(
                    value = vm.totalStudents.toString(),
                    label = "Students",
                    modifier = Modifier.weight(1f)
                )

                LecturerSummaryCard(
                    value = vm.completedCount.toString(),
                    label = "Completed Count",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = LightBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Quick Action",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CardWhite
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Create a new task for your students",
                        fontSize = 14.sp,
                        color = CardWhite
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomButton(
                        text = "Create Task",
                        clickButton = {
                            navController.navigate(NavScreen.CreateTask.route)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = CardWhite,
                        contentColor = LightBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Recent Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))
            // most upcoming tasks and only 2 on dashboard
            vm.tasks
                .sortedBy { task ->
                    java.time.LocalDate.parse(
                        task.deadline,
                        java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                }
            vm.tasks.take(2).forEach { task ->
                LecturerRecentTaskCard(
                    title = task.title,
                    module = task.moduleCode,
                    progress = "View progress",
                    onClick = {
                        navController.navigate(
                            NavScreen.TaskProgress.createRoute(task.id)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }
}

@Composable
fun LecturerSummaryCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}
