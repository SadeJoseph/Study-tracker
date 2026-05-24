package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.navigation.NavScreen
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.presentation.components.LecturerBottomNavBar
import com.example.myapplication.presentation.screens.student.ProfileCard
import com.example.myapplication.ui.theme.*
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.presentation.screens.profile.ProfileViewModel
import com.example.myapplication.presentation.components.CustomTextField
@Composable
fun LecturerProfileScreen(
    navController: NavController,
    vm: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadProfile()
    }
    val authRepository = AuthRepository()

    Scaffold(
        bottomBar = { LecturerBottomNavBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .padding(20.dp)
        ) {
            Text("Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Manage your lecturer account", fontSize = 16.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(24.dp))

            ProfileCard(
                name = vm.user?.name ?: "",
                role = vm.user?.role ?: "",
                email = vm.user?.email ?: "",
                id = vm.user?.uid ?: "",
                module = vm.user?.moduleCodes?.joinToString(", ") ?: ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                hintText = "Add module code",
                text = vm.newModuleCode,
                onValueChange = { vm.onModuleCodeChange(it) },
                contentDescription = "Add Module Code"
            )

            Spacer(modifier = Modifier.height(12.dp))

            CustomButton(
                text = "Add Module",
                clickButton = {
                    vm.addModuleCode()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = vm.newModuleCode.isNotBlank()
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomButton(
                text = "Log Out",
                clickButton = {
                    authRepository.logout()
                    navController.navigate(NavScreen.Login.route){
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}