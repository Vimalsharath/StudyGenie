package com.studygenie.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studygenie.app.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = viewModel()
) {
    val state by viewModel.analyticsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Study Analytics") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Summary Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total",
                        value = state.totalTasks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Done",
                        value = state.completedTasks.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Rate",
                        value = "${(state.completionRate * 100).toInt()}%",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Weekly Progress Chart (Simple Visualization)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Weekly Study Progress",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (state.dailyProgress.isEmpty()) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No data yet", style = MaterialTheme.typography.bodyMedium)
                                }
                            } else {
                                state.dailyProgress.forEach { (_, count) ->
                                    val maxCount = state.dailyProgress.maxOf { it.second }.takeIf { it > 0 } ?: 1
                                    val barHeight = (count.toFloat() / maxCount * 100).dp
                                    Box(
                                        modifier = Modifier
                                            .width(30.dp)
                                            .height(barHeight)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Subject Analysis
            item {
                Text(
                    text = "Subject Breakdown",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (state.subjectBreakdown.isEmpty()) {
                item {
                    Text("Add tasks to see subject analysis.")
                }
            } else {
                items(state.subjectBreakdown.toList()) { (subject, count) ->
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "📚 $subject", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "$count Tasks", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
