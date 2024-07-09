package com.example.sokerihiiri.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.sokerihiiri.ui.screens.settings.SettingsDefaultsScreen
import com.example.sokerihiiri.ui.screens.settings.SettingsScreen

fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
) {
    navigation(
        route = Screens.Settings.route,
        startDestination = Screens.Settings.Main.route
    ) {
        composable(Screens.Settings.Main.route) {
            SettingsScreen()
        }
        composable(Screens.Settings.Defaults.route) {
            SettingsDefaultsScreen(
                navController = navController,
            )
        }
    }
}