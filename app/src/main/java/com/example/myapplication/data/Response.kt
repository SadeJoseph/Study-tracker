package com.example.myapplication.data

sealed class Response {
    data object Startup : Response()
    data object Loading : Response()
    data object Success : Response()
    data class Failure(val exception: Exception) : Response()
}