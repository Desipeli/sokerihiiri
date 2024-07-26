package com.example.sokerihiiri.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.sokerihiiri.ui.screens.browse.BrowseScreen
import com.example.sokerihiiri.ui.screens.browse.injections.BrowseInjectionsScreen
import com.example.sokerihiiri.ui.screens.browse.meals.BrowseMealsScreen
import com.example.sokerihiiri.ui.screens.browse.measurements.BrowseMeasurementsScreen
import com.example.sokerihiiri.ui.screens.insulin.InsulinScreen
import com.example.sokerihiiri.ui.screens.meal.MealScreen
import com.example.sokerihiiri.ui.screens.measurement.MeasurementScreen


fun NavGraphBuilder.browseNavigation(
    navController: NavController
) {
    navigation(
        route = Screens.Browse.route,
        startDestination = Screens.Browse.Main.route
    ) {
        composable(route = Screens.Browse.Main.route) {
            BrowseScreen(navController = navController)
        }
        navigation(
            route = Screens.Browse.Measurements.route,
            startDestination = Screens.Browse.Measurements.Main.route
        ) {
            composable(route = Screens.Browse.Measurements.Main.route) {
                BrowseMeasurementsScreen(
                    navController = navController
                )
            }
            composable(route = Screens.Browse.Measurements.Measurement.route + "/{id}") {
                MeasurementScreen(
                    navController = navController,
                    id = it.arguments?.getString("id")
                )
            }
        }
        navigation(
            route = Screens.Browse.Injections.route,
            startDestination = Screens.Browse.Injections.Main.route
        ) {
            composable(route = Screens.Browse.Injections.Main.route) {
                BrowseInjectionsScreen(
                    navController = navController
                )
            }
            composable(route = Screens.Browse.Injections.Injection.route + "/{id}") {
                InsulinScreen(
                    id = it.arguments?.getString("id")
                )
            }
        }
        navigation(
            route = Screens.Browse.Meals.route,
            startDestination = Screens.Browse.Meals.Main.route
        ) {
            composable(route = Screens.Browse.Meals.Main.route) {
                BrowseMealsScreen(
                    navController = navController
                )
            }
            composable(route = Screens.Browse.Meals.Meal.route + "/{id}") {
                MealScreen(
                    navController = navController,
                    id = it.arguments?.getString("id")
                )
            }
        }
    }
}