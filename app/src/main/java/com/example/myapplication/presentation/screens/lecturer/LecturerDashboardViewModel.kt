package com.example.myapplication.presentation.screens.lecturer

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

class LecturerDashboardViewModel : ViewModel() {

    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()
    private val taskRepository = TaskRepository()

    var tasks by mutableStateOf<List<ModuleTask>>(emptyList())
        private set

    var totalTasks by mutableIntStateOf(0)
        private set

    var totalStudents by mutableIntStateOf(0)
        private set

    var completedCount by mutableIntStateOf(0)
        private set

    fun loadDashboard() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser() ?: return@launch
            val user = userRepository.getUser(currentUser.uid) ?: return@launch

            tasks = taskRepository.getTasksForModules(user.moduleCodes)
            totalTasks = tasks.size

            val studentIds = mutableSetOf<String>()
            var completed = 0

            user.moduleCodes.forEach { moduleCode ->
                val students = userRepository.getStudentsForModule(moduleCode)
                studentIds.addAll(students.map { it.uid })
            }

            tasks.forEach { task ->
                val progressList = taskRepository.getProgressForTask(task.id)
                completed += progressList.count { it.status == "COMPLETED" }
            }

            totalStudents = studentIds.size
            completedCount = completed
        }
    }
}