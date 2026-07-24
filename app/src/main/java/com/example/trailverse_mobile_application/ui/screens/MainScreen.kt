package com.example.trailverse_mobile_application.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import com.example.trailverse_mobile_application.navigation.Routes
import com.example.trailverse_mobile_application.ui.components.BottomNavBar

@Composable
fun MainScreen(
    onLocationClick: (String) -> Unit,
    onAddLocation: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(bottomNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onLocationClick = onLocationClick,
                    onAddLocation = onAddLocation,
                    onProfileClick = { bottomNavController.navigate(Routes.PROFILE) }
                )
            }
            composable(Routes.EXPLORE) {
                ExploreScreen(onLocationClick = onLocationClick)
            }
            composable(Routes.SAVED) {
                SavedScreen(onLocationClick = onLocationClick)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onBack = { bottomNavController.navigate(Routes.HOME) })
            }
        }
    }
}