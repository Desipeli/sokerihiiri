package com.example.sokerihiiri.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sokerihiiri.ui.screens.MainScreen
import com.example.sokerihiiri.ui.screens.insulin.InsulinScreen
import com.example.sokerihiiri.ui.screens.meal.MealScreen
import com.example.sokerihiiri.ui.screens.measurement.MeasurementScreen

@Composable
fun AppNavHost(navController: NavHostController, innerPadding: PaddingValues) {
    // Navigointi https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#1
    // https://nameisjayant.medium.com/nested-navigation-in-jetpack-compose-597ecdc6eebb
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route,
        modifier = androidx.compose.ui.Modifier
            .padding(innerPadding)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        composable(route = Screens.Main.route) {
            MainScreen( navController = navController)
        }
        browseNavigation(
            navController = navController,
        )
        settingsNavigation(
            navController,
        )
        composable(route = Screens.Measurement.route) {
            MeasurementScreen(
                navController = navController
            )
        }
        composable(route = Screens.Insulin.route) {
            InsulinScreen(
                navController = navController
            )
        }
        composable(route = Screens.Meal.route) {
            MealScreen(
                navController = navController
            )
        }
    }
}