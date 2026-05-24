package com.example.myapplication.data.model

data class AppUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = "",
    val moduleCodes: List<String> = emptyList()
)