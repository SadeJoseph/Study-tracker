package com.example.myapplication.presentation.screens.student

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.launch

class StudentDashboardViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val taskRepository = TaskRepository()

    var tasks by mutableStateOf<List<ModuleTask>>(emptyList())
        private set

    var completedCount by mutableIntStateOf(0)
        private set

    var inProgressCount by mutableIntStateOf(0)
        private set

    var notStartedCount by mutableIntStateOf(0)
        private set

    var progressPercent by mutableIntStateOf(0)
        private set

    var taskStatuses by mutableStateOf<Map<String, String>>(emptyMap())
        private set

    fun loadDashboard() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser() ?: return@launch
            val user = userRepository.getUser(currentUser.uid) ?: return@launch

            tasks = taskRepository.getTasksForModules(user.moduleCodes)

            val progressList = taskRepository.getTaskProgressForUser(currentUser.uid)
            taskStatuses = progressList.associate {
                it.taskId to it.status
            }
            completedCount = progressList.count { it.status == "COMPLETED" }
            inProgressCount = progressList.count { it.status == "IN_PROGRESS" }

            notStartedCount = tasks.size - completedCount - inProgressCount

            progressPercent =
                if (tasks.isEmpty()) 0 else (completedCount * 100) / tasks.size
        }
    }
}