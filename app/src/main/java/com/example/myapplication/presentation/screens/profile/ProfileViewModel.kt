package com.example.myapplication.presentation.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.Response
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.model.AppUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    var user by mutableStateOf<AppUser?>(null)
        private set

    var newModuleCode by mutableStateOf("")
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onModuleCodeChange(value: String) {
        newModuleCode = value.uppercase()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser() ?: return@launch
            user = userRepository.getUser(currentUser.uid)
        }
    }

    fun addModuleCode() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser()

            if (currentUser == null || newModuleCode.isBlank()) {
                _uiEvents.send("Module code is required")
                return@launch
            }

            val response = userRepository.addModuleCode(
                uid = currentUser.uid,
                moduleCode = newModuleCode.trim()
            )

            when (response) {
                is Response.Success -> {
                    newModuleCode = ""
                    loadProfile()
                    _uiEvents.send("Module added")
                }

                is Response.Failure -> {
                    _uiEvents.send(
                        response.exception.message ?: "Unable to add module"
                    )
                }

                else -> {}
            }
        }
    }
}