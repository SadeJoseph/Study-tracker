package com.example.myapplication.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.presentation.components.CustomTextField
import com.example.myapplication.ui.theme.CardWhite
import com.example.myapplication.ui.theme.LightGray
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.presentation.screens.login.LoginViewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.myapplication.data.Response
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToStudentDashboard: () -> Unit = {},
    navigateToLecturerDashboard: () -> Unit = {},
    navigateToSignUpScreen: () -> Unit = {},
    vm: LoginViewModel = viewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }
    if (vm.loginResponse is Response.Success && vm.userRole != null) {
        LaunchedEffect(vm.loginResponse, vm.userRole) {
            when (vm.userRole) {
                "STUDENT" -> navigateToStudentDashboard()
                "LECTURER" -> navigateToLecturerDashboard()
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "StudyTracker",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Track your module tasks easily",
                        fontSize = 16.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    CustomTextField(
                        hintText = "Email",
                        text = vm.loginUiState.email,
                        onValueChange = {
                            vm.onChange(email = it)
                        },
                        errorMessage = "Please enter a valid email",
                        errorPresent = vm.loginUiState.emailIsInvalid(),
                        contentDescription = "Email Login"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        hintText = "Password",
                        text = vm.loginUiState.password,
                        onValueChange = {
                            vm.onChange(password = it)
                        },
                        isPasswordField = true,
                        errorMessage = "Password must be at least 6 characters",
                        errorPresent = vm.loginUiState.passwordIsInvalid(),
                        contentDescription = "Password Login"
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    CustomButton(
                        text = "Login",
                        clickButton = {
                            vm.login()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = vm.isFormValid()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { navigateToSignUpScreen() }) {
                        Text(
                            text = "Don't have an account? Sign up",
                            color = TextSecondary,

                            )
                    }
                }
            }
        }
    }
}
