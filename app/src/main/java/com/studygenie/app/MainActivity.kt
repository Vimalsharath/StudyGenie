package com.studygenie.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studygenie.app.presentation.navigation.AppNavigation
import com.studygenie.app.ui.theme.StudyGenieTheme
import com.studygenie.app.viewmodel.SettingsViewModel
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val isDynamicColor by settingsViewModel.isDynamicColor.collectAsState()

            StudyGenieTheme(
                darkTheme = isDarkMode,
                dynamicColor = isDynamicColor
            ) {
                AppNavigation()
            }
        }
    }
}
