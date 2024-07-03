package com.example.sokerihiiri

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.sokerihiiri.repository.SokerihiiriDatabase
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.ui.components.BottomBar
import com.example.sokerihiiri.ui.screens.measurement.MeasurementViewModel
import com.example.sokerihiiri.ui.screens.measurement.MeasurementViewModelFactory
import com.example.sokerihiiri.ui.components.TopBar
import com.example.sokerihiiri.ui.screens.browse.BrowseScreen
import com.example.sokerihiiri.ui.screens.insulin.InsulinScreen
import com.example.sokerihiiri.ui.screens.insulin.InsulinViewModel
import com.example.sokerihiiri.ui.screens.insulin.InsulinViewModelFactory
import com.example.sokerihiiri.ui.screens.MainScreen
import com.example.sokerihiiri.ui.screens.MealScreen
import com.example.sokerihiiri.ui.screens.measurement.MeasurementScreen
import com.example.sokerihiiri.ui.screens.SettingsScreen
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModel
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModelFactory
import com.example.sokerihiiri.ui.screens.browse.measurements.BrowseMeasurementsScreen

sealed class Screens(val route: String, val title: String) {
    object Main : Screens(route = "main", title="Sokerihiiri")
    object Measurement : Screens(route = "measurement", title="Mittaus")
    object Insulin : Screens(route = "insulin", title="Insuliini")
    object Meal : Screens(route = "meal", title="Ateria")
    object Browse : Screens(route = "browse", title="Selaa") {
        object Main : Screens(route = "browse_main", title="Selaa")
        object Measurements : Screens(route = "browse_measurements", title="Mittaukset")
    }
    object Settings : Screens(route = "settings", title="Asetukset")
}

enum class BrowseRoutes(val title: String) {
    Main(title = "Selaa"),
    Measurements(title = "Mittaus"),
}

@Composable
fun SokerihiiriApp(
    navController: NavHostController = rememberNavController(),
) {
    // Navigointi https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation#1
    // https://nameisjayant.medium.com/nested-navigation-in-jetpack-compose-597ecdc6eebb

    val database = SokerihiiriDatabase.getDatabase(context = LocalContext.current)

    val repository = SokerihiiriRepository(
        bloodSugarMeasurementDao = database.bloodSugarMeasurementDao(),
        insulinInjectionDao = database.insulinInjectionDao(),
        mealDao = database.mealDao(),
    )

    val measurementViewModel: MeasurementViewModel = viewModel(
        factory = MeasurementViewModelFactory(repository = repository)
    )
    val insulinViewModel: InsulinViewModel = viewModel(
        factory = InsulinViewModelFactory(repository = repository)
    )
    val browseViewModel: BrowseViewModel = viewModel(
        factory = BrowseViewModelFactory(repository = repository)
    )


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
        topBar = {
            TopBar(
                title = { Text(text = "Sokerihiiri") }
            )
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Main.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = Screens.Main.route) {
                MainScreen( navController = navController)
            }

            navigation(
                route = Screens.Browse.route,
                startDestination = Screens.Browse.Main.route
            ) {
                composable(route = Screens.Browse.Main.route) {
                    BrowseScreen(navController = navController)
                }
                composable(route = Screens.Browse.Measurements.route) {
                    BrowseMeasurementsScreen(browseViewModel = browseViewModel)
                }
            }
            composable(route = Screens.Measurement.route) {
                MeasurementScreen(
                    measurementViewModel = measurementViewModel,
                )
            }
            composable(route = Screens.Insulin.route) {
                InsulinScreen(
                    insulinViewModel = insulinViewModel
                )
            }
            composable(route = Screens.Meal.route) {
                MealScreen()
            }
            composable(route = Screens.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun BrowseNavHost(
    navController: NavHostController,
    browseViewModel: BrowseViewModel) {

    NavHost(
        navController = navController,
        startDestination = BrowseRoutes.Main.name
    ) {
        composable(route = BrowseRoutes.Main.name) {
            BrowseScreen(navController = navController)
        }
        composable(route = BrowseRoutes.Measurements.name) {
            BrowseMeasurementsScreen(browseViewModel = browseViewModel)
        }
    }
}