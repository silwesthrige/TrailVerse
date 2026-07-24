package com.example.trailverse_mobile_application.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun TrailVerseNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LOGIN) {
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
                onAddLocation = { navController.navigate(Routes.ADD_LOCATION) }
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