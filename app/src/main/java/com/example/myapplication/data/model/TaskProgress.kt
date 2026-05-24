package com.example.myapplication.data.model

data class TaskProgress(
    val id: String = "",
    val userId: String = "",
    val taskId: String = "",
    val status: String = "NOT_STARTED"
)