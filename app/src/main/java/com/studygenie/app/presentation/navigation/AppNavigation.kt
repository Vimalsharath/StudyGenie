package com.studygenie.app.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.studygenie.app.presentation.screens.LoginScreen
import com.studygenie.app.presentation.screens.RegisterScreen
import com.studygenie.app.presentation.screens.SplashScreen

import com.studygenie.app.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavigation(viewModel: AuthViewModel = viewModel()) {

    val navController = rememberNavController()
    val isLoggedIn = viewModel.currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login"
    ) {

        composable("splash") {
            SplashScreen()
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            MainScreen(
                onLogoutClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }

}