package com.example.myapplication.presentation.screens.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.navigation.NavScreen
import com.example.myapplication.presentation.components.BottomNavBar
import com.example.myapplication.presentation.components.CustomButton
import com.example.myapplication.ui.theme.*
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.presentation.screens.profile.ProfileViewModel
import com.example.myapplication.presentation.components.CustomTextField


@Composable
fun StudentProfileScreen(
    navController: NavController,
    vm: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        vm.loadProfile()
    }
    val authRepository = AuthRepository()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(LightGray)
                .padding(20.dp)
        ) {
            Text("Profile", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Manage your student account", fontSize = 16.sp, color = TextSecondary)

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
                    navController.navigate(NavScreen.Login.route) {
                        // so back button cannot return to dashboard after logoutt
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProfileCard(
    name: String,
    role: String,
    email: String,
    id: String,
    module: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(role, fontSize = 15.sp, color = TextSecondary)

            Spacer(modifier = Modifier.height(20.dp))

            ProfileDetail("Email", email)
            ProfileDetail("ID", id)
            ProfileDetail("Module", module)
        }
    }
}

@Composable
fun ProfileDetail(label: String, value: String) {
    Text(label, fontSize = 13.sp, color = TextSecondary)
    Text(value, fontSize = 15.sp, color = TextPrimary)

    Spacer(modifier = Modifier.height(14.dp))
}
