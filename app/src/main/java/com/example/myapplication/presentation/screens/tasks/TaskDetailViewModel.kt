package com.example.myapplication.presentation.screens.tasks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.launch
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.Response
import com.example.myapplication.data.model.TaskProgress
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class TaskDetailViewModel : ViewModel() {

    private val taskRepository = TaskRepository()
    private val authRepository = AuthRepository()

    var task by mutableStateOf<ModuleTask?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set
    var progress by mutableStateOf<TaskProgress?>(null)
        private set
    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun loadTask(taskId: String) {

        viewModelScope.launch {
            isLoading = true

            try {
                task = taskRepository.getTaskById(taskId)
                val currentUser = authRepository.currentUser()

                if (currentUser != null) {

                    progress = taskRepository.getTaskProgress(
                        currentUser.uid,
                        taskId
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
    fun updateTaskStatus(
        taskId: String,
        status: String
    ) {

        viewModelScope.launch {

            try {

                val currentUser = authRepository.currentUser()

                if (currentUser == null) {
                    _uiEvents.send("User not logged in")
                    return@launch
                }

                val taskProgress = TaskProgress(
                    userId = currentUser.uid,
                    taskId = taskId,
                    status = status
                )

                val response =
                    taskRepository.saveTaskProgress(taskProgress)

                when (response) {

                    is Response.Success -> {

                        progress = taskProgress

                        _uiEvents.send(
                            "Task status updated"
                        )
                    }

                    is Response.Failure -> {

                        _uiEvents.send(
                            response.exception.message
                                ?: "Unable to update task"
                        )
                    }

                    else -> {}
                }

            } catch (e: Exception) {

                _uiEvents.send(
                    e.message ?: "Unable to update task"
                )
            }
        }
    }

}