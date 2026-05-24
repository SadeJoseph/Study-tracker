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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.presentation.components.CustomTextField
import com.example.myapplication.ui.theme.LightBlue
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun CreateTaskScreen(
    navController: NavController,
    vm: CreateTaskViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    LaunchedEffect(Unit) {
        vm.loadLecturerModules()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                text = "Create Task",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create a module task for students",
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            CustomTextField(
                hintText = "Task title",
                text = vm.title,
                onValueChange = { vm.onTitleChange(it) },
                contentDescription = "Task Title"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "COMP0044",
                text = vm.moduleCode,
                onValueChange = { vm.onModuleCodeChange(it) },
                contentDescription = "Module Code"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Description",
                text = vm.description,
                onValueChange = { vm.onDescriptionChange(it) },
                contentDescription = "Description"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Deadline e.g. 26/05/2026",
                text = vm.deadline,
                onValueChange = { vm.onDeadlineChange(it) },
                contentDescription = "Deadline"
            )

            Spacer(modifier = Modifier.height(28.dp))

            CustomButton(
                text = "Create Task",
                clickButton = {
                    vm.createTask()
                },
                enabled = vm.isFormValid(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}