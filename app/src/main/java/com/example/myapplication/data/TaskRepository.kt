package com.example.myapplication.data

import com.example.myapplication.data.model.ModuleTask
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.myapplication.data.model.TaskProgress

class TaskRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createTask(task: ModuleTask): Response {
        return try {
            val document = firestore.collection("tasks").document()

            val taskWithId = task.copy(
                id = document.id
            )

            document.set(taskWithId).await()

            Response.Success
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    suspend fun getTasksCreatedBy(userId: String): List<ModuleTask> {
        val snapshot = firestore.collection("tasks")
            .whereEqualTo("createdBy", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(ModuleTask::class.java)
        }
    }

    suspend fun getTasksForModules(
        moduleCodes: List<String>
    ): List<ModuleTask> {

        val snapshot = firestore.collection("tasks")
            .whereIn("moduleCode", moduleCodes)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(ModuleTask::class.java)
        }
    }

    suspend fun getTaskById(taskId: String): ModuleTask? {

        val document = firestore.collection("tasks")
            .document(taskId)
            .get()
            .await()

        return document.toObject(ModuleTask::class.java)
    }

    suspend fun saveTaskProgress(
        progress: TaskProgress
    ): Response {

        return try {

            val document = firestore
                .collection("task_progress")
                .document("${progress.userId}_${progress.taskId}")

            val progressWithId = progress.copy(
                id = document.id
            )

            document.set(progressWithId).await()

            Response.Success

        } catch (e: Exception) {

            Response.Failure(e)
        }
    }

    suspend fun getTaskProgress(
        userId: String,
        taskId: String
    ): TaskProgress? {

        val document = firestore
            .collection("task_progress")
            .document("${userId}_${taskId}")
            .get()
            .await()

        return document.toObject(TaskProgress::class.java)
    }
    suspend fun getTaskProgressForUser(
        userId: String
    ): List<TaskProgress> {

        val snapshot = firestore.collection("task_progress")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(TaskProgress::class.java)
        }
    }
    suspend fun getProgressForTask(
        taskId: String
    ): List<TaskProgress> {

        val snapshot = firestore.collection("task_progress")
            .whereEqualTo("taskId", taskId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(TaskProgress::class.java)
        }
    }
    suspend fun deleteTask(taskId: String): Response {
        return try {
            val progressSnapshot = firestore.collection("task_progress")
                .whereEqualTo("taskId", taskId)
                .get()
                .await()

            progressSnapshot.documents.forEach { document ->
                document.reference.delete().await()
            }

            firestore.collection("tasks")
                .document(taskId)
                .delete()
                .await()

            Response.Success
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}