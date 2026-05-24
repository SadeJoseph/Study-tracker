package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.presentation.components.CustomTextField
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.performTextInput

@RunWith(AndroidJUnit4::class)
class CustomTextFieldTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun textFieldDisplaysHintAndText() {

        rule.setContent {
            CustomTextField(
                hintText = "Email",
                text = "test@email.com",
                onValueChange = {},
                errorMessage = "",
                errorPresent = false,
                contentDescription = ""
            )
        }

        rule.onNodeWithText("Email")
            .assertIsDisplayed()

        rule.onNodeWithText("test@email.com")
            .assertIsDisplayed()
    }
    @Test
    fun errorMessageDisplaysWhenErrorPresentIsTrue() {

        rule.setContent {
            CustomTextField(
                hintText = "Email",
                text = "",
                onValueChange = {},
                errorMessage = "Email is required",
                errorPresent = true,
                contentDescription = ""
            )
        }

        rule.onNodeWithText("Email is required")
            .assertIsDisplayed()
    }
    @Test
    fun textFieldUpdatesWhenUserInputsText() {

        var inputText = ""

        rule.setContent {
            CustomTextField(
                hintText = "Email",
                text = inputText,
                onValueChange = {
                    inputText = it
                },
                errorMessage = "",
                errorPresent = false,
                contentDescription = ""
            )
        }

        rule.onNodeWithText("Email")
            .performTextInput("test@email.com")

        assert(inputText == "test@email.com")
    }

}

