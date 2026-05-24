package com.example.myapplication.presentation.screens.signup

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
import com.example.myapplication.data.model.AppUser
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val auth = FirebaseAuth.getInstance()
    var loginUiState by mutableStateOf(LoginUiState())
        private set

    var name by mutableStateOf("")
        private set

    var role by mutableStateOf("STUDENT")
        private set

    var moduleCode by mutableStateOf("COMP60044")
        private set

    var signUpResponse by mutableStateOf<Response>(Response.Startup)
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onNameChange(value: String) {
        name = value
    }

    fun onRoleChange(value: String) {
        role = value.uppercase()
    }

    fun onModuleCodeChange(value: String) {
        moduleCode = value.uppercase()
    }

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
        return name.trim().isNotBlank() &&
                moduleCode.trim().isNotBlank() &&
                role.trim().uppercase() in listOf("STUDENT", "LECTURER") &&
                loginUiState.isValid()
    }

    private fun clearForm() {
        loginUiState = LoginUiState()
        name = ""
        role = "STUDENT"
        moduleCode = "COMP60044"
    }
    fun signUp() {
        viewModelScope.launch {
            signUpResponse = Response.Loading

            val authResponse = authRepository.signUp(
                email = loginUiState.email.trim(),
                password = loginUiState.password
            )

            when (authResponse) {
                is Response.Success -> {
                    val firebaseUser = auth.currentUser

                    if (firebaseUser != null) {
                        val user = AppUser(
                            uid = firebaseUser.uid,
                            name = name.trim(),
                            email = loginUiState.email.trim(),
                            role = role.trim().uppercase(),
                            moduleCodes = listOf(moduleCode.trim().uppercase())
                        )

                        val firestoreResponse = userRepository.saveUser(user)

                        signUpResponse = firestoreResponse

                        when (firestoreResponse) {
                            is Response.Success -> {
                                clearForm()
                                _uiEvents.send("Account created successfully")
                            }

                            is Response.Failure -> {
                                _uiEvents.send(
                                    firestoreResponse.exception.message
                                        ?: "Unable to save user details"
                                )
                            }

                            else -> {}
                        }
                    } else {
                        signUpResponse = Response.Failure(Exception("Missing user information"))
                        _uiEvents.send("Missing user information")
                    }
                }

                is Response.Failure -> {
                    signUpResponse = authResponse
                    _uiEvents.send(
                        authResponse.exception.message ?: "Unable to create account"
                    )
                }

                else -> {}
            }
        }
    }
}