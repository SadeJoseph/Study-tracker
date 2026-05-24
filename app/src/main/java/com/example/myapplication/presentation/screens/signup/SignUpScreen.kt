package com.example.myapplication.presentation.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.presentation.components.CustomTextField
import com.example.myapplication.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    vm: SignUpViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        vm.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = "Create Account",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign up to start tracking module tasks",
                fontSize = 16.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            CustomTextField(
                hintText = "Full name",
                text = vm.name,
                onValueChange = { vm.onNameChange(it) },
                errorMessage = "Name is required",
                errorPresent = vm.name.isBlank(),
                contentDescription = "Full Name"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Email",
                text = vm.loginUiState.email,
                onValueChange = { vm.onChange(email = it) },
                errorMessage = "Please enter a valid email",
                errorPresent = vm.loginUiState.emailIsInvalid(),
                contentDescription = "Email Sign Up"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Password",
                text = vm.loginUiState.password,
                onValueChange = { vm.onChange(password = it) },
                isPasswordField = true,
                errorMessage = "Password must be at least 6 characters",
                errorPresent = vm.loginUiState.passwordIsInvalid(),
                contentDescription = "Password Sign Up"

            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Role e.g. STUDENT or LECTURER",
                text = vm.role,
                onValueChange = { vm.onRoleChange(it) },
                errorMessage = "Role must be STUDENT or LECTURER",
                errorPresent = vm.role !in listOf("STUDENT", "LECTURER"),
                contentDescription = "Role"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                hintText = "Module code",
                text = vm.moduleCode,
                onValueChange = { vm.onModuleCodeChange(it) },
                errorMessage = "Module code is required",
                errorPresent = vm.moduleCode.isBlank(),
                contentDescription = "Module Sign Up"
            )

            Spacer(modifier = Modifier.height(28.dp))

            CustomButton(
                text = "Sign Up",
                clickButton = {
                    vm.signUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = vm.isFormValid(),
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = navigateBack) {
                Text(
                    text = "Already have an account? Back to login",
                    color = TextSecondary
                )
            }
        }
    }
}