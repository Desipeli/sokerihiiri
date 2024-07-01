package com.example.sokerihiiri

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sokerihiiri.ui.components.TopBar
import com.example.sokerihiiri.ui.screens.BrowseScreen
import com.example.sokerihiiri.ui.screens.InsulinScreen
import com.example.sokerihiiri.ui.screens.MainScreen
import com.example.sokerihiiri.ui.screens.MealScreen
import com.example.sokerihiiri.ui.screens.MeasurementScreen
import com.example.sokerihiiri.ui.screens.SettingsScreen

enum class Routes(val title: String) {
    Main(title = "Sokerihiiri"),
    Measurement(title = "Mittaus"),
    Insulin(title = "Insuliini"),
    Meal(title = "Ateria"),
    Browse(title = "Selaa"),
    Settings(title = "Asetukset")
}

@Composable
fun SokerihiiriApp(
    navController: NavHostController = rememberNavController(),
) {
    // Navigointi https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#1
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = Routes.valueOf(
        backStackEntry.value?.destination?.route ?: Routes.Main.name
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = { Text(text = "Sokerihiiri") }
            )
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Routes.Main.name) {
                MainScreen( navController = navController)
            }
            composable(route = Routes.Measurement.name) {
                MeasurementScreen()
            }
            composable(route = Routes.Insulin.name) {
                InsulinScreen()
            }
            composable(route = Routes.Meal.name) {
                MealScreen()
            }
            composable(route = Routes.Browse.name) {
                BrowseScreen()
            }
            composable(route = Routes.Settings.name) {
                SettingsScreen()
            }
        }
    }
}