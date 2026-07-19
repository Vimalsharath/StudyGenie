package com.studygenie.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studygenie.app.data.local.AppDatabase
import com.studygenie.app.data.local.UserEntity
import com.studygenie.app.data.model.Task
import com.studygenie.app.repository.AuthRepository
import com.studygenie.app.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

data class ProfileState(
    val user: UserEntity? = null,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val completionRate: Int = 0,
    val totalStudyHours: Float = 0f,
    val productivityScore: Int = 0,
    val strongestSubject: String = "N/A"
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    
    private val authRepository: AuthRepository by lazy {
        val database = AppDatabase.getDatabase(application)
        AuthRepository(database.userDao())
    }
    
    private val taskRepository: TaskRepository by lazy {
        val database = AppDatabase.getDatabase(application)
        TaskRepository(database.taskDao())
    }

    val profileState: StateFlow<ProfileState> by lazy {
        combine(
            authRepository.currentUser?.let { authRepository.getLocalUser(it.uid) } ?: flowOf(null),
            taskRepository.allTasks
        ) { user, tasks ->
            calculateProfileState(user, tasks)
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileState()
        )
    }

    private fun calculateProfileState(user: UserEntity?, tasks: List<Task>): ProfileState {
        if (tasks.isEmpty()) return ProfileState(user = user)

        val completed = tasks.count { it.isCompleted }
        val total = tasks.size
        val rate = if (total > 0) (completed.toFloat() / total * 100).toInt() else 0
        val hours = completed * 1.5f 
        
        val subjectBreakdown = tasks.groupBy { it.subject }.mapValues { it.value.size }
        val strongest = subjectBreakdown.maxByOrNull { it.value }?.key ?: "N/A"

        return ProfileState(
            user = user,
            totalTasks = total,
            completedTasks = completed,
            pendingTasks = total - completed,
            completionRate = rate,
            totalStudyHours = hours,
            productivityScore = rate,
            strongestSubject = strongest
        )
    }
}
