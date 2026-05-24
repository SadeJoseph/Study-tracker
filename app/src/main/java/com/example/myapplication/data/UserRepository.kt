package com.example.myapplication.data

import com.example.myapplication.data.model.AppUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun saveUser(user: AppUser): Response {
        return try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()

            Response.Success
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
    suspend fun getUserRole(uid: String): String? {
        val document = firestore.collection("users")
            .document(uid)
            .get()
            .await()
        return document.getString("role")
    }

    suspend fun getUser(uid: String): AppUser? {

        val document = firestore.collection("users")
            .document(uid)
            .get()
            .await()

        return document.toObject(AppUser::class.java)
    }
    suspend fun getStudentsForModule(
        moduleCode: String
    ): List<AppUser> {

        val snapshot = firestore.collection("users")
            .whereArrayContains("moduleCodes", moduleCode)
            .whereEqualTo("role", "STUDENT")
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(AppUser::class.java)
        }
    }
    suspend fun addModuleCode(
        uid: String,
        moduleCode: String
    ): Response {
        return try {
            val userRef = firestore.collection("users").document(uid)

            val user = getUser(uid)

            val updatedModules = user?.moduleCodes
                ?.plus(moduleCode.uppercase())
                ?.distinct()
                ?: listOf(moduleCode.uppercase())

            userRef.update("moduleCodes", updatedModules).await()

            Response.Success
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}