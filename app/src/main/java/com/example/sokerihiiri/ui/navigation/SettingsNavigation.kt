package com.example.sokerihiiri.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.sokerihiiri.ui.screens.settings.SettingsControlDataScreen
import com.example.sokerihiiri.ui.screens.settings.SettingsDefaultsScreen
import com.example.sokerihiiri.ui.screens.settings.SettingsNotificationsScreen
import com.example.sokerihiiri.ui.screens.settings.SettingsScreen

fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    // Asetusten navigointigraafi

    navigation(
        route = Screens.Settings.route,
        startDestination = Screens.Settings.Main.route
    ) {
        composable(Screens.Settings.Main.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screens.Settings.Defaults.route) {
            SettingsDefaultsScreen(
                snackbarHostState = snackbarHostState
            )
        }
        composable(Screens.Settings.ControlData.route) {
            SettingsControlDataScreen(
                snackbarHostState = snackbarHostState)
        }
        composable(Screens.Settings.Notifications.route) {
            SettingsNotificationsScreen(
                snackbarHostState = snackbarHostState)
        }
    }
}