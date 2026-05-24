package com.example.myapplication.presentation.screens.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class StudentTaskListViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val taskRepository = TaskRepository()

    var tasks by mutableStateOf<List<ModuleTask>>(emptyList())
        private set
    var taskStatuses by mutableStateOf<Map<String, String>>(emptyMap())
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun loadTasks() {

        viewModelScope.launch {

            isLoading = true

            try {

                val currentUser = authRepository.currentUser()

                if (currentUser == null) {
                    _uiEvents.send("User not logged in")
                    return@launch
                }

                val userDocument =
                    userRepository.getUser(currentUser.uid)

                if (userDocument == null) {
                    _uiEvents.send("User not found")
                    return@launch
                }

                tasks = taskRepository.getTasksForModules(
                    userDocument.moduleCodes
                )
                val progressList = taskRepository.getTaskProgressForUser(
                    currentUser.uid
                )

                taskStatuses = progressList.associate {
                    it.taskId to it.status
                }

            } catch (e: Exception) {
                _uiEvents.send(
                    e.message ?: "Unable to load tasks"
                )
            } finally {

                isLoading = false
            }
        }
    }
}