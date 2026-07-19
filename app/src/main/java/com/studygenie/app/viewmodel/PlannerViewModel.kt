package com.studygenie.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.studygenie.app.data.local.AppDatabase
import com.studygenie.app.data.local.worker.ReminderWorker
import com.studygenie.app.data.model.Task
import com.studygenie.app.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PlannerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: TaskRepository by lazy {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        TaskRepository(taskDao)
    }
    
    private val workManager = WorkManager.getInstance(application)

    val tasks: StateFlow<List<Task>> by lazy {
        repository.allTasks
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
            scheduleReminder(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
            cancelReminder(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
            if (task.isCompleted) {
                cancelReminder(task)
            } else {
                scheduleReminder(task)
            }
        }
    }

    private fun scheduleReminder(task: Task) {
        if (task.isCompleted) return
        try {
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            val taskDate = sdf.parse("${task.date} ${task.time}")
            val currentTime = System.currentTimeMillis()

            if (taskDate != null && taskDate.time > currentTime) {
                val delay = taskDate.time - currentTime
                val data = Data.Builder()
                    .putString("taskTitle", task.title)
                    .putString("subject", task.subject)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .addTag("task_${task.id}")
                    .build()

                workManager.enqueue(workRequest)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cancelReminder(task: Task) {
        workManager.cancelAllWorkByTag("task_${task.id}")
    }
}
