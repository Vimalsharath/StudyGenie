package com.studygenie.app.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studygenie.app.viewmodel.PomodoroViewModel
import com.studygenie.app.viewmodel.TimerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    onBackClick: () -> Unit,
    viewModel: PomodoroViewModel = viewModel()
) {
    val timeLeft by viewModel.timeLeft.collectAsState()
    val initialTime by viewModel.initialTime.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    
    val progress = if (initialTime > 0) timeLeft.toFloat() / initialTime else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Focus Mode") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(300.dp)
            ) {
                // Background Circle (Track)
                val trackColor = MaterialTheme.colorScheme.surfaceVariant
                val progressColor = when (timerState) {
                    TimerState.FINISHED -> MaterialTheme.colorScheme.primary
                    TimerState.RUNNING -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.secondary
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = trackColor,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = progressColor,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (timerState == TimerState.FINISHED) "DONE!" else viewModel.formatTime(timeLeft),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (timerState == TimerState.FINISHED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when (timerState) {
                            TimerState.RUNNING -> "FOCUSING..."
                            TimerState.PAUSED -> "PAUSED"
                            TimerState.FINISHED -> "SESSION COMPLETE"
                            else -> "READY TO START"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            // Timer Presets
            if (timerState == TimerState.IDLE) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    listOf(15, 25, 45, 60).forEach { mins ->
                        SuggestionChip(
                            onClick = { viewModel.setSessionTime(mins) },
                            label = { Text("${mins}m") },
                            icon = { Icon(Icons.Default.Timer, null, modifier = Modifier.size(16.dp)) }
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reset Button
                OutlinedIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                }

                // Play/Pause Button
                FilledIconButton(
                    onClick = {
                        if (timerState == TimerState.RUNNING) viewModel.pauseTimer()
                        else viewModel.startTimer()
                    },
                    modifier = Modifier.size(80.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = if (timerState == TimerState.RUNNING) 
                            MaterialTheme.colorScheme.secondary 
                        else 
                            MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        imageVector = if (timerState == TimerState.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Start/Pause",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Session Goal: ${initialTime / 60} minutes",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
