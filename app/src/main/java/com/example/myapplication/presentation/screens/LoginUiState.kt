package com.example.myapplication.presentation.screens

import android.util.Patterns

data class LoginUiState(
    val email: String = "",
    val password: String = ""
) {
    fun emailIsInvalid(): Boolean {
        return email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun passwordIsInvalid(): Boolean {
        return password.isNotBlank() && password.length < 6
    }

    fun isValid(): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                !emailIsInvalid() &&
                !passwordIsInvalid()
    }
}