package com.studygenie.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("dashboard", "Home", Icons.Default.Home)
    object Planner : BottomNavItem("planner_tab", "Planner", Icons.Default.DateRange)
    object AI : BottomNavItem("ai_tab", "AI", Icons.Default.AutoAwesome)
    object Analytics : BottomNavItem("analytics_tab", "Analytics", Icons.Default.BarChart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    object Focus : BottomNavItem("focus", "Focus", Icons.Default.Timer)
}
