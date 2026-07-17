package com.studygenie.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextDecoration
import com.studygenie.app.data.model.Task

import androidx.compose.ui.graphics.Color
import com.studygenie.app.data.model.Priority

@Composable
fun TaskCard(
    task: Task,
    onComplete: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = task.subject,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                textDecoration =
                if (task.isCompleted)
                    TextDecoration.LineThrough
                else
                    TextDecoration.None
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("📅 ${task.date}")
                    Text("🕒 ${task.time}")
                }

                val (priorityEmoji, priorityColor) = when (task.priority) {
                    Priority.HIGH -> "🔥 High" to Color(0xFFD32F2F)
                    Priority.MEDIUM -> "🟡 Medium" to Color(0xFFFBC02D)
                    Priority.LOW -> "🟢 Low" to Color(0xFF388E3C)
                }

                Text(
                    text = priorityEmoji,
                    color = priorityColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                IconButton(
                    onClick = onComplete
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Complete",
                        tint = if (task.isCompleted) Color(0xFF388E3C) else LocalContentColor.current
                    )

                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onDelete) {

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )

                }

            }

        }
    }

}