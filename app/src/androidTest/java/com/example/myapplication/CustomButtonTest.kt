package com.example.myapplication

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.presentation.components.CustomButton
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CustomButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun buttonDisplaysTextAndExecutesClickEvent() {

        var buttonClicked = false
        rule.setContent {

            CustomButton(
                text = "Create Task",
                clickButton = {
                    buttonClicked = true
                }
            )
        }

        rule.onNode(
            hasContentDescription("Create Task button")
        )
            .assertExists()
            .performClick()

        assertTrue(buttonClicked)
    }

    @Test
    fun buttonIsDisabledWhenEnabledIsFalse() {

        rule.setContent {
            CustomButton(
                text = "Disabled Button",
                clickButton = {},
                enabled = false
            )
        }

        rule.onNode(
            hasContentDescription("Disabled Button button")
        )
            .assertExists()
            .assertIsNotEnabled()
    }
}



