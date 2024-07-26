package com.example.sokerihiiri.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sokerihiiri.ui.components.BottomBar
import com.example.sokerihiiri.ui.components.TopBar
import com.example.sokerihiiri.ui.navigation.AppNavHost
import com.example.sokerihiiri.ui.screens.BrowseViewModel
import com.example.sokerihiiri.ui.screens.injections.insulin.InsulinViewModel
import com.example.sokerihiiri.ui.screens.meals.meal.MealViewModel
import com.example.sokerihiiri.ui.screens.measurements.measurement.MeasurementViewModel
import com.example.sokerihiiri.ui.screens.settings.SettingsViewModel

val LocalMeasurementViewModel = staticCompositionLocalOf<MeasurementViewModel> {
    error("No MeasurementViewModel provided")
}
val LocalInsulinViewModel = staticCompositionLocalOf<InsulinViewModel> {
    error("No InsulinInjectionViewModel provided")
}
val LocalMealViewModel = staticCompositionLocalOf<MealViewModel> {
    error("No MealViewModel provided")
}
val LocalSettingsViewModel = staticCompositionLocalOf<SettingsViewModel> {
    error("No SettingsViewModel provided")
}
val LocalBrowseViewModel = staticCompositionLocalOf<BrowseViewModel> {
    error("No BrowseViewModel provided")
}

@Composable
fun AppContent(
    navController: NavHostController = rememberNavController(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val measurementViewModel: MeasurementViewModel = hiltViewModel()
    val insulinViewModel: InsulinViewModel = hiltViewModel()
    val mealViewModel: MealViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val browseViewModel: BrowseViewModel = hiltViewModel()

    CompositionLocalProvider (
        LocalMeasurementViewModel provides measurementViewModel,
        LocalInsulinViewModel provides insulinViewModel,
        LocalMealViewModel provides mealViewModel,
        LocalSettingsViewModel provides settingsViewModel,
        LocalBrowseViewModel provides browseViewModel
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomBar(navController, snackbarHostState) },
            topBar = {
                TopBar(navController = navController)
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
            AppNavHost(navController, snackbarHostState, innerPadding)
        }
    }
}