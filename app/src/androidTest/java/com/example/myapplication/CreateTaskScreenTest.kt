package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.presentation.screens.lecturer.CreateTaskScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateTaskScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun createTaskButtonIsDisabledWhenFieldsAreEmpty() {

        rule.setContent {

            CreateTaskScreen(
                navController = rememberTestNavController()
            )
        }

        rule.onNode(
            hasContentDescription("Create Task button")
        )
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun enteringValidTaskDataEnablesCreateTaskButton() {

        rule.setContent {

            CreateTaskScreen(
                navController = rememberTestNavController()
            )
        }

        rule.onNode(
            hasContentDescription("Task Title")
        ).performTextInput("Mobile App Assignment")

        rule.onNode(
            hasContentDescription("Module Code")
        ).performTextInput("COMP0044")

        rule.onNode(
            hasContentDescription("Description")
        ).performTextInput("Build Android coursework app")

        rule.onNode(
            hasContentDescription("Deadline")
        ).performTextInput("26/05/2026")

        rule.onNode(
            hasContentDescription("Create Task button")
        )
            .assertIsEnabled()
    }

    @Test
    fun invalidDeadlineKeepsCreateTaskButtonDisabled() {

        rule.setContent {

            CreateTaskScreen(
                navController = rememberTestNavController()
            )
        }

        rule.onNode(
            hasContentDescription("Task Title")
        ).performTextInput("Assignment")

        rule.onNode(
            hasContentDescription("Module Code")
        ).performTextInput("COMP0044")

        rule.onNode(
            hasContentDescription("Description")
        ).performTextInput("Android app")

        rule.onNode(
            hasContentDescription("Deadline")
        ).performTextInput("01/01/2020")

        rule.onNode(
            hasContentDescription("Create Task button")
        )
            .assertIsNotEnabled()
    }
}