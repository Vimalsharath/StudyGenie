package com.studygenie.app.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
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
    val timerState by viewModel.timerState.collectAsState()
    val progress = timeLeft.toFloat() / (25 * 60f)

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
                // Background Circle
                val trackColor = MaterialTheme.colorScheme.surfaceVariant
                val progressColor = MaterialTheme.colorScheme.tertiary

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
                        text = viewModel.formatTime(timeLeft),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = if (timerState == TimerState.RUNNING) "FOCUSING" else "READY",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

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
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Icon(
                        imageVector = if (timerState == TimerState.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Start/Pause",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Placeholder for music/sounds
                OutlinedIconButton(
                    onClick = { /* Sound Settings */ },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp, 
                        contentDescription = "Sounds"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = "Session: 25 mins",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
