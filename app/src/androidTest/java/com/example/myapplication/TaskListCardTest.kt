package com.example.myapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.presentation.components.TaskListCard
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskListCardTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun taskCardDisplaysTaskDetails() {
        rule.setContent {
            TaskListCard(
                title = "Mobile App Assignment",
                module = "COMP0044",
                dueDate = "26/05/2026",
                status = "IN_PROGRESS",
                onClick = {}
            )
        }

        rule.onNodeWithText("Mobile App Assignment").assertIsDisplayed()
        rule.onNodeWithText("COMP0044").assertIsDisplayed()
        rule.onNodeWithText("26/05/2026").assertIsDisplayed()
        rule.onNodeWithText("In Progress").assertIsDisplayed()
    }

    @Test
    fun taskCardExecutesClickEvent() {
        var clicked = false

        rule.setContent {
            TaskListCard(
                title = "Mobile App Assignment",
                module = "COMP0044",
                dueDate = "26/05/2026",
                status = "COMPLETED",
                onClick = {
                    clicked = true
                }
            )
        }

        rule.onNodeWithText("Mobile App Assignment")
            .performClick()

        assertTrue(clicked)
    }
}