package com.studygenie.app.data.model

import androidx.compose.ui.text.style.TextDecoration

data class Task(
    val id: Int,
    val subject: String,
    val title: String,
    val date: String,
    val time: String,
    val priority: Priority,
    val isCompleted: Boolean = false
)

enum class Priority {
    HIGH,
    MEDIUM,
    LOW
}