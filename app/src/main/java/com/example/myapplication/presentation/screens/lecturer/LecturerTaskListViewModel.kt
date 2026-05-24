package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.Response

class LecturerTaskListViewModel: ViewModel()
{
    private val taskRepository = TaskRepository()
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    var tasks by mutableStateOf<List<ModuleTask>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var taskProgressText by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun loadTasks() {
        viewModelScope.launch {
            isLoading = true

            val currentUser = authRepository.currentUser()

            if (currentUser == null) {
                _uiEvents.send("User not logged in")
                isLoading = false
                return@launch
            }

            try {
                tasks = taskRepository.getTasksCreatedBy(currentUser.uid)
                val progressTextMap = mutableMapOf<String, String>()

                tasks.forEach { task ->

                    val students = userRepository.getStudentsForModule(task.moduleCode)

                    val progressList = taskRepository.getProgressForTask(task.id)

                    val completedCount = progressList.count {
                        it.status == "COMPLETED"
                    }

                    val totalStudents = students.size

                    progressTextMap[task.id] =
                        "$completedCount/$totalStudents completed"
                }

                taskProgressText = progressTextMap
            } catch (e: Exception) {
                _uiEvents.send(e.message ?: "Unable to load tasks")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            val response = taskRepository.deleteTask(taskId)

            when (response) {
                is Response.Success -> {
                    loadTasks()
                    _uiEvents.send("Task deleted")
                }

                is Response.Failure -> {
                    _uiEvents.send(response.exception.message ?: "Unable to delete task")
                }

                else -> {}
            }
        }
    }
}
