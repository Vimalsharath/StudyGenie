package com.studygenie.app.presentation.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studygenie.app.presentation.components.*
import com.studygenie.app.viewmodel.AuthViewModel
import com.studygenie.app.viewmodel.PlannerViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTaskClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onFocusModeClick: () -> Unit,
    onNotificationsClick: () -> Unit, // Add this
    onSettingsClick: () -> Unit, // Add this
    onLogoutClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    plannerViewModel: PlannerViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val localUser by authViewModel.localUser.collectAsState()
    val tasks by plannerViewModel.tasks.collectAsState()

    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "$greeting, ${localUser?.name ?: "Scholar"} 👋",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Small progress every day leads to big success.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = onLogoutClick) {
                        Surface(
                            modifier = Modifier.size(32.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.padding(4.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Progress Hero Card
            item {
                ProgressHeroCard(
                    progress = progress,
                    completed = completedTasks,
                    total = totalTasks
                )
            }

            // 2. Quick Actions
            item {
                Column {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        QuickActionCard(
                            icon = Icons.Default.Add,
                            title = "Add Task",
                            desc = "Plan your goals",
                            onClick = onAddTaskClick
                        )
                        QuickActionCard(
                            icon = Icons.Default.AutoAwesome,
                            title = "AI Assistant",
                            desc = "Ask Gemini",
                            onClick = onAiAssistantClick
                        )
                        QuickActionCard(
                            icon = Icons.Default.CalendarMonth,
                            title = "Calendar",
                            desc = "View schedule",
                            onClick = { /* TODO */ }
                        )
                        QuickActionCard(
                            icon = Icons.Default.Timer,
                            title = "Focus Mode",
                            desc = "Pomodoro",
                            onClick = onFocusModeClick
                        )
                    }
                }
            }

            // 3. Pomodoro Focus Card
            item {
                PomodoroCard()
            }

            // 4. AI Recommendation
            item {
                AiRecommendationCard(onGenerate = onAiAssistantClick)
            }

            // 5. Upcoming Tasks Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onAddTaskClick) {
                        Text("See All")
                    }
                }
            }

            val upcomingTasks = tasks.filter { !it.isCompleted }.take(3)
            if (upcomingTasks.isEmpty()) {
                item {
                    Text(
                        text = "All caught up! No pending tasks. 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(upcomingTasks) { task ->
                    PremiumTaskCard(task)
                }
            }

            // 6. Achievements
            item {
                AchievementRow()
            }

            // 7. Motivation Quote
            item {
                MotivationCard()
            }
            
            // Extra spacing at the bottom
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}
