package com.studygenie.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studygenie.app.presentation.components.*
import com.studygenie.app.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.profileState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            // 1. Header
            item {
                ProfileHeader(user = state.user)
            }

            // 2. Statistics
            item {
                StatsGrid(
                    total = state.totalTasks,
                    completed = state.completedTasks,
                    hours = state.totalStudyHours,
                    rate = state.completionRate
                )
            }

            // 3. Achievements (Placeholders for now)
            item {
                AchievementRow()
            }

            // 4. Settings
            item {
                SettingsGroup(
                    onLogout = onLogoutClick,
                    onNotificationsClick = onNotificationsClick,
                    onSettingsClick = onSettingsClick
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
