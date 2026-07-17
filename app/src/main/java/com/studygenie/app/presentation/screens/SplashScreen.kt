package com.studygenie.app.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SplashScreen() {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4F46E5),
            Color(0xFF06B6D4)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "📚",
            style = MaterialTheme.typography.displayLarge
        )

        Text(
            text = "StudyGenie",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Learn Smarter. Study Better.",
            color = Color.White
        )
    }
}