package com.example.myapplication.presentation.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.CardWhite
import com.example.myapplication.ui.theme.LightBlue
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.SuccessGreen
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.ui.theme.WarningOrange
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import com.example.myapplication.presentation.components.BottomNavBar
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.myapplication.navigation.NavScreen
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
@Composable
fun StudentDashboardScreen(
    navController: NavController,
    vm: StudentDashboardViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadDashboard()
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Dashboard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = "Track your academic progress",
                fontSize = 15.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Completed",
                    value = vm.completedCount.toString(),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "In Progress",
                    value = vm.inProgressCount.toString(),
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Not Started",
                    value = vm.notStartedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProgressCard(
                progressPercent = vm.progressPercent
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Upcoming Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))
            //no tasks

            if (vm.tasks.isEmpty()) {

                Text(
                    text = "No upcoming tasks",
                    color = TextSecondary
                )

            } else {

// take only 2 tasks for dashboard and 2 nearest todays date
                vm.tasks
                    .sortedBy { task ->
                        java.time.LocalDate.parse(
                            task.deadline,
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                    }
                    .take(2).forEach { task ->
                        DashboardTaskCard(
                            title = task.title,
                            module = task.moduleCode,
                            dueDate = task.deadline,
                            status = vm.taskStatuses[task.id] ?: "NOT_STARTED",
                            onClick = {
                                navController.navigate(NavScreen.TaskDetail.createRoute(task.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
            }
        }
    }}
@Composable
fun SummaryCard(
    title: String,
    value: String,
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
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun ProgressCard(
    progressPercent: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LightBlue),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Overall Progress",
                fontSize = 14.sp,
                color = CardWhite
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${progressPercent}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = CardWhite
            )
        }
    }
}

@Composable
fun DashboardTaskCard(
    title: String,
    module: String,
    dueDate: String,
    status: String,
    onClick: () -> Unit = {}
) {
    val statusColor = when (status) {
        "COMPLETED" -> SuccessGreen
        "IN_PROGRESS" -> WarningOrange
        else -> TextSecondary
    }

    Card(
        modifier = Modifier.fillMaxWidth() .clickable{ onClick()},
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = module,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = dueDate,
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (status) {
                    "COMPLETED" -> "Completed"
                    "IN_PROGRESS" -> "In Progress"
                    else -> "Not Started"
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }
    }
}