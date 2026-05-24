package com.example.myapplication

import com.example.myapplication.presentation.screens.LoginUiState
import org.junit.Assert
import org.junit.Test

class LoginUiStateTest {

    @Test
    fun validEmailAndPasswordReturnsTrue() {
        val state = LoginUiState(
            email = "student@test.com",
            password = "password123"
        )

        Assert.assertTrue(state.isValid())
    }

    @Test
    fun invalidEmailReturnsFalse() {
        val state = LoginUiState(
            email = "student",
            password = "password123"
        )

        Assert.assertFalse(state.isValid())
        Assert.assertTrue(state.emailIsInvalid())
    }

    @Test
    fun shortPasswordReturnsFalse() {
        val state = LoginUiState(
            email = "student@test.com",
            password = "123"
        )

        Assert.assertFalse(state.isValid())
        Assert.assertTrue(state.passwordIsInvalid())
    }
}