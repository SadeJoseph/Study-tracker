package com.example.myapplication.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.Response
import com.example.myapplication.presentation.screens.LoginUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.UserRepository

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    var loginResponse by mutableStateOf<Response>(Response.Startup)
        private set
    var userRole by mutableStateOf<String?>(null)
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onChange(
        email: String = loginUiState.email,
        password: String = loginUiState.password
    ) {
        loginUiState = loginUiState.copy(
            email = email,
            password = password
        )
    }

    fun isFormValid(): Boolean {
        return loginUiState.isValid()
    }

    fun login() {
        viewModelScope.launch {
            loginResponse = Response.Loading

            val response = authRepository.login(
                email = loginUiState.email.trim(),
                password = loginUiState.password
            )

            when (response) {
                is Response.Success -> {
                    val uid = authRepository.currentUser()?.uid

                    if (uid != null) {
                        val role = userRepository.getUserRole(uid)

                        userRole = role
                        loginResponse = Response.Success

                        _uiEvents.send("Logged in successfully")
                    } else {
                        loginResponse = Response.Failure(Exception("User not found"))
                        _uiEvents.send("User not found")
                    }
                }

                is Response.Failure -> {
                    loginResponse = response

                    loginUiState = loginUiState.copy(
                        password = ""
                    )

                    _uiEvents.send(
                        response.exception.message ?: "Unable to log in"
                    )
                }

                else -> {}
            }
        }
    }
}