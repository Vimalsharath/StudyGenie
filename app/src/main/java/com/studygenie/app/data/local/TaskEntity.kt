package com.studygenie.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.studygenie.app.data.model.Priority
import com.studygenie.app.data.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val title: String,
    val date: String,
    val time: String,
    val priority: Priority,
    val isCompleted: Boolean = false
)

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        subject = subject,
        title = title,
        date = date,
        time = time,
        priority = priority,
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        subject = subject,
        title = title,
        date = date,
        time = time,
        priority = priority,
        isCompleted = isCompleted
    )
}
