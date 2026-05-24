package com.example.myapplication.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun signUp(
        email: String,
        password: String
    ): Response {

        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success

        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Response {

        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success

        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun currentUser() = auth.currentUser
}