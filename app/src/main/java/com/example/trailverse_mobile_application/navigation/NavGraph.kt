package com.example.trailverse_mobile_application.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trailverse_mobile_application.ui.screens.AddLocationScreen
import com.example.trailverse_mobile_application.ui.screens.DetailScreen
import com.example.trailverse_mobile_application.ui.screens.LoginScreen
import com.example.trailverse_mobile_application.ui.screens.MainScreen
import com.example.trailverse_mobile_application.ui.screens.RegisterScreen
import com.example.trailverse_mobile_application.ui.screens.SplashScreen
import com.example.trailverse_mobile_application.viewmodel.AuthViewModel

@Composable
fun TrailVerseNavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val destination = if (authViewModel.isLoggedIn()) Routes.MAIN else Routes.LOGIN
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Routes.MAIN) {
            MainScreen(
                onLocationClick = { id -> navController.navigate(Routes.detail(id)) },
                onAddLocation = { navController.navigate(Routes.ADD_LOCATION) },
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ADD_LOCATION) {
            AddLocationScreen(onBack = { navController.popBackStack() })
        }
        composable(
            Routes.DETAIL,
            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId") ?: ""
            DetailScreen(locationId = locationId, onBack = { navController.popBackStack() })
        }
    }
}