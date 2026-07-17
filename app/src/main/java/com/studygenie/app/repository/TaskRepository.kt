package com.studygenie.app.repository

import com.studygenie.app.data.local.TaskDao
import com.studygenie.app.data.local.toDomain
import com.studygenie.app.data.local.toEntity
import com.studygenie.app.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }
}
