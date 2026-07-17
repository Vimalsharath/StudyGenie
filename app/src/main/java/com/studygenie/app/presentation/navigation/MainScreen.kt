package com.studygenie.app.presentation.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.studygenie.app.presentation.screens.*

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Planner,
        BottomNavItem.AI,
        BottomNavItem.Analytics,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(BottomNavItem.Planner.route) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                DashboardScreen(
                    onAddTaskClick = { navController.navigate(BottomNavItem.Planner.route) },
                    onAiAssistantClick = { navController.navigate(BottomNavItem.AI.route) },
                    onFocusModeClick = { navController.navigate(BottomNavItem.Focus.route) },
                    onNotificationsClick = { navController.navigate("notifications") },
                    onSettingsClick = { navController.navigate("settings") },
                    onLogoutClick = onLogoutClick
                )
            }
            composable(BottomNavItem.Planner.route) {
                PlannerScreen(onBackClick = { navController.popBackStack() })
            }
            composable(BottomNavItem.AI.route) {
                AiAssistantScreen(onBackClick = { navController.popBackStack() })
            }
            composable(BottomNavItem.Analytics.route) {
                AnalyticsScreen(onBackClick = { navController.popBackStack() })
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onLogoutClick = onLogoutClick,
                    onNotificationsClick = { navController.navigate("notifications") },
                    onSettingsClick = { navController.navigate("settings") }
                )
            }
            composable(BottomNavItem.Focus.route) {
                FocusScreen(onBackClick = { navController.popBackStack() })
            }
            composable("notifications") {
                NotificationsScreen(onBackClick = { navController.popBackStack() })
            }
            composable("settings") {
                SettingsScreen(
                    onBackClick = { navController.popBackStack() },
                    onEditProfileClick = { navController.navigate("edit_profile") },
                    onChangePasswordClick = { navController.navigate("change_password") }
                )
            }
            composable("edit_profile") {
                EditProfileScreen(onBackClick = { navController.popBackStack() })
            }
            composable("change_password") {
                ChangePasswordScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
