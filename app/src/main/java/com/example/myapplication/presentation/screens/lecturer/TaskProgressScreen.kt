package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.ui.theme.CardWhite
import com.example.myapplication.ui.theme.LightBlue
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.SuccessGreen
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.ui.theme.WarningOrange
import com.example.myapplication.presentation.components.CustomButton
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color

@Composable
fun TaskProgressScreen(
    navController: NavController,
    taskId: String,
    vm: TaskProgressViewModel = viewModel()
) {
    // Load lecturer task analytics from Firestore
    LaunchedEffect(Unit) {
        vm.loadTaskProgress(taskId)
    }
    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = "← Back",
                    color = LightBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Task Progress",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = vm.task?.title ?: "",
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            vm.studentProgressList.forEach { studentProgress ->

                StudentProgressCard(
                    studentName = studentProgress.studentName,
                    status = studentProgress.status
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}
    @Composable
    fun StudentProgressCard(
        studentName: String,
        status: String
    ) {
        val statusColor = when (status) {
            "COMPLETED" -> SuccessGreen
            "IN_PROGRESS" -> WarningOrange
            else -> TextSecondary
        }

        val displayStatus = when (status) {
            "COMPLETED" -> "Completed"
            "IN_PROGRESS" -> "In Progress"
            else -> "Not Started"
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = studentName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = displayStatus,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            }
        }
    }


