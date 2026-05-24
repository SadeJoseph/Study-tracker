package com.example.myapplication.presentation.screens.lecturer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.AuthRepository
import com.example.myapplication.data.Response
import com.example.myapplication.data.TaskRepository
import com.example.myapplication.data.UserRepository
import com.example.myapplication.data.model.ModuleTask
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CreateTaskViewModel : ViewModel() {

    private val taskRepository = TaskRepository()
    private val authRepository = AuthRepository()

    var title by mutableStateOf("")
        private set

    var moduleCode by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var deadline by mutableStateOf("")
        private set

    var createTaskResponse by mutableStateOf<Response>(Response.Startup)
        private set

    var lecturerModuleCodes by mutableStateOf<List<String>>(emptyList())
        private set

    private val _uiEvents = Channel<String>()
    val uiEvents = _uiEvents.receiveAsFlow()

    fun onTitleChange(value: String) {
        title = value
    }

    fun onModuleCodeChange(value: String) {
        moduleCode = value.uppercase()
    }

    fun onDescriptionChange(value: String) {
        description = value
    }

    fun onDeadlineChange(value: String) {
        deadline = value
    }


    fun isFormValid(): Boolean {
        return title.trim().isNotBlank() &&
                moduleCode.trim().isNotBlank() &&
                description.trim().isNotBlank() &&
                deadline.trim().isNotBlank() &&
                isDeadlineTodayOrFuture()
    }

    fun createTask() {
        viewModelScope.launch {
            createTaskResponse = Response.Loading

            val currentUser = authRepository.currentUser()

            if (currentUser == null) {
                createTaskResponse = Response.Failure(Exception("User not logged in"))
                _uiEvents.send("User not logged in")
                return@launch
            }

            val task = ModuleTask(
                title = title.trim(),
                description = description.trim(),
                moduleCode = moduleCode.trim().uppercase(),
                deadline = deadline.trim(),
                createdBy = currentUser.uid
            )
// validation
            if (!lecturerModuleCodes.contains(moduleCode.trim().uppercase())) {
                _uiEvents.send("You can only create tasks for your assigned modules")
                createTaskResponse = Response.Startup
                return@launch
            }

            if (!isDeadlineTodayOrFuture()) {
                _uiEvents.send("Deadline must be today or a future date")
                createTaskResponse = Response.Startup
                return@launch
            }

            val response = taskRepository.createTask(task)

            createTaskResponse = response

            when (response) {
                is Response.Success -> {
                    clearForm()
                    _uiEvents.send("Task created successfully")
                }

                is Response.Failure -> {
                    _uiEvents.send(
                        response.exception.message ?: "Unable to create task"
                    )
                }

                else -> {}
            }
        }
    }

    private fun clearForm() {
        title = ""
        moduleCode = ""
        description = ""
        deadline = ""
    }
    private val userRepository = UserRepository()

    fun loadLecturerModules() {
        viewModelScope.launch {
            val currentUser = authRepository.currentUser() ?: return@launch
            val user = userRepository.getUser(currentUser.uid) ?: return@launch

            lecturerModuleCodes = user.moduleCodes
            moduleCode = user.moduleCodes.firstOrNull() ?: ""
        }
    }

    private fun isDeadlineTodayOrFuture(): Boolean {
        return try {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val selectedDate = java.time.LocalDate.parse(deadline.trim(), formatter)
            val today = java.time.LocalDate.now()

            !selectedDate.isBefore(today)
        } catch (e: Exception) {
            false
        }
    }

}