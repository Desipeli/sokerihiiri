package com.example.sokerihiiri.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sokerihiiri.ui.screens.MainScreen
import com.example.sokerihiiri.ui.screens.injections.BrowseInjectionsScreen
import com.example.sokerihiiri.ui.screens.meals.BrowseMealsScreen
import com.example.sokerihiiri.ui.screens.measurements.BrowseMeasurementsScreen
import com.example.sokerihiiri.ui.screens.injections.insulin.InsulinScreen
import com.example.sokerihiiri.ui.screens.meals.meal.MealScreen
import com.example.sokerihiiri.ui.screens.measurements.measurement.MeasurementScreen
import com.example.sokerihiiri.ui.screens.others.BrowseOthersScreen
import com.example.sokerihiiri.ui.screens.others.other.OtherScreen

@Composable
fun AppNavHost(navController: NavHostController, snackbarHostState: SnackbarHostState, innerPadding: PaddingValues) {
    // Navigointi https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#1
    // https://nameisjayant.medium.com/nested-navigation-in-jetpack-compose-597ecdc6eebb
    NavHost(
        navController = navController,
        startDestination = Screens.Main.route,
        modifier = androidx.compose.ui.Modifier
            .padding(innerPadding)
            .padding(start = 8.dp, end = 8.dp)
    )
    {
        composable(route = Screens.Main.route) {
            MainScreen( navController = navController)
        }
        composable(route = Screens.Measurements.Main.route) {
            BrowseMeasurementsScreen(navController = navController)
        }
        composable(route = Screens.Measurements.NewMeasurement.route) {
            MeasurementScreen(navController = navController)
        }
        composable(route = Screens.Measurements.EditMeasurement.route + "/{id}") {
            MeasurementScreen(
                navController = navController,
                id = it.arguments?.getString("id"))
        }
        composable(route = Screens.Injections.Main.route) {
            BrowseInjectionsScreen(navController = navController)
        }
        composable(route = Screens.Injections.NewInjection.route) {
            InsulinScreen()
        }
        composable(route = Screens.Injections.EditInjection.route + "/{id}") {
            InsulinScreen(id = it.arguments?.getString("id"))
        }
        composable(route = Screens.Meals.Main.route) {
            BrowseMealsScreen(navController = navController)
        }
        composable(route = Screens.Meals.NewMeal.route) {
            MealScreen(navController = navController)
        }
        composable(route = Screens.Meals.EditMeal.route + "/{id}") {
            MealScreen(
                navController = navController,
                id = it.arguments?.getString("id"))
        }
        composable(route = Screens.Others.Main.route) {
            BrowseOthersScreen(navController = navController)
        }
        composable(route = Screens.Others.NewOther.route) {
            OtherScreen()
        }
        composable(route = Screens.Others.EditOther.route + "/{id}") {
            OtherScreen(id = it.arguments?.getString("id"))
        }

        settingsNavigation(
            navController,
            snackbarHostState,
        )
    }
}