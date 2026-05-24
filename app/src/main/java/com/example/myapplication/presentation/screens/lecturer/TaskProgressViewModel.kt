package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.launch


data class StudentProgressUiState(
    val studentName: String,
    val status: String
)
class TaskProgressViewModel : ViewModel() {

    private val taskRepository = TaskRepository()
    private val userRepository = UserRepository()

    var task by mutableStateOf<ModuleTask?>(null)
        private set

    var totalStudents by mutableIntStateOf(0)
        private set

    var completedCount by mutableIntStateOf(0)
        private set

    var inProgressCount by mutableIntStateOf(0)
        private set

    var notStartedCount by mutableIntStateOf(0)
        private set

    var studentProgressList by mutableStateOf<List<StudentProgressUiState>>(emptyList())
        private set

    fun loadTaskProgress(taskId: String) {

        viewModelScope.launch {

            val loadedTask =
                taskRepository.getTaskById(taskId)

            task = loadedTask

            if (loadedTask == null) return@launch

            val students =
                userRepository.getStudentsForModule(
                    loadedTask.moduleCode
                )

            totalStudents = students.size

            val progressList =
                taskRepository.getProgressForTask(taskId)

            studentProgressList = students.map { student ->

                val progress = progressList.find {
                    it.userId == student.uid
                }

                StudentProgressUiState(
                    studentName = student.name,
                    status = progress?.status ?: "NOT_STARTED"
                )
            }

            completedCount = progressList.count {
                it.status == "COMPLETED"
            }

            inProgressCount = progressList.count {
                it.status == "IN_PROGRESS"
            }

            notStartedCount =
                totalStudents -
                        completedCount -
                        inProgressCount
        }
    }
}