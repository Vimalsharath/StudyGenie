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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PlannerViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    private val workManager = WorkManager.getInstance(application)
    val tasks: StateFlow<List<Task>>

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        tasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.addTask(task)
            scheduleReminder(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            cancelReminder(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
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
                android.util.Log.d("Reminder", "Scheduling reminder for ${task.title} in ${delay/1000} seconds")

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
