@file:OptIn(ExperimentalMaterial3Api::class)
package com.studygenie.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studygenie.app.data.model.Task
import com.studygenie.app.data.model.Priority
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.studygenie.app.presentation.components.TaskCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.ui.Alignment

import com.studygenie.app.viewmodel.PlannerViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(onBackClick: () -> Unit, viewModel: PlannerViewModel = viewModel()) {
    // ...
    // Keep existing dialog logic exactly as is
    // ...
    // (Actual file starts here for implementation)
    var subject by remember { mutableStateOf("") }
    var taskTitle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()
    val datePickerState = rememberDatePickerState()
    var priority by remember { mutableStateOf("Medium") }

    val tasks by viewModel.tasks.collectAsState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        date = datePickerState.selectedDateMillis
                                ?.let {
                                    java.text.SimpleDateFormat(
                                        "dd MMM yyyy",
                                        java.util.Locale.getDefault()
                                    ).format(java.util.Date(it))
                                } ?: ""
                        showDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        time = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Planner") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true }
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true }
                ) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Time") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = priority == "High",
                        onClick = { priority = "High" },
                        label = { Text("🔥 High") }
                    )
                    FilterChip(
                        selected = priority == "Medium",
                        onClick = { priority = "Medium" },
                        label = { Text("🟡 Medium") }
                    )
                    FilterChip(
                        selected = priority == "Low",
                        onClick = { priority = "Low" },
                        label = { Text("🟢 Low") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val newTask = Task(
                            id = 0,
                            subject = subject,
                            title = taskTitle,
                            date = date,
                            time = time,
                            priority = when(priority) {
                                "High" -> Priority.HIGH
                                "Low" -> Priority.LOW
                                else -> Priority.MEDIUM
                            }
                        )
                        viewModel.addTask(newTask)
                        subject = ""
                        taskTitle = ""
                        date = ""
                        time = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("SAVE TASK")
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Your Tasks",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (tasks.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📚", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "No tasks yet", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "Start planning your study goals!")
                    }
                }
            } else {
                items(tasks) { task ->
                    TaskCard(
                        task = task,
                        onComplete = { viewModel.updateTask(task.copy(isCompleted = !task.isCompleted)) },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }
    }
}

