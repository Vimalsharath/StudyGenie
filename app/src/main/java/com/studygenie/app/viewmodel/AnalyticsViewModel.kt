package com.studygenie.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studygenie.app.data.local.AppDatabase
import com.studygenie.app.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

data class AnalyticsState(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val completionRate: Float = 0f,
    val subjectBreakdown: Map<String, Int> = emptyMap(),
    val dailyProgress: List<Pair<String, Int>> = emptyList()
)

class AnalyticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository by lazy {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        TaskRepository(taskDao)
    }

    val analyticsState: StateFlow<AnalyticsState> by lazy {
        repository.allTasks.map { tasks ->
            val total = tasks.size
            val completed = tasks.count { it.isCompleted }
            val rate = if (total > 0) completed.toFloat() / total else 0f
            
            val breakdown = tasks.groupBy { it.subject }.mapValues { it.value.size }
            
            val daily = tasks.filter { it.isCompleted }
                .groupBy { it.date }
                .mapValues { it.value.size }
                .toList()
                .takeLast(7)

            AnalyticsState(
                totalTasks = total,
                completedTasks = completed,
                completionRate = rate,
                subjectBreakdown = breakdown,
                dailyProgress = daily
            )
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnalyticsState()
        )
    }
}
