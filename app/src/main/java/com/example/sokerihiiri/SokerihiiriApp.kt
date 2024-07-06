package com.example.sokerihiiri

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.sokerihiiri.ui.screens.meals.MealScreen
import com.example.sokerihiiri.ui.screens.measurement.MeasurementScreen
import com.example.sokerihiiri.ui.screens.SettingsScreen
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModel
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModelFactory
import com.example.sokerihiiri.ui.screens.browse.injections.BrowseInjectionsScreen
import com.example.sokerihiiri.ui.screens.browse.meals.BrowseMealsScreen
import com.example.sokerihiiri.ui.screens.browse.measurements.BrowseMeasurementsScreen
import com.example.sokerihiiri.ui.screens.meals.MealViewModel
import com.example.sokerihiiri.ui.screens.meals.MealViewModelFactory

sealed class Screens(val route: String, val title: String) {
    object Main : Screens(route = "main", title="Sokerihiiri")
    object Measurement : Screens(route = "measurement", title="Mittaus")
    object Insulin : Screens(route = "insulin", title="Insuliini")
    object Meal : Screens(route = "meal", title="Ateria")
    object Browse : Screens(route = "browse", title="Selaa") {
        object Main : Screens(route = "browse_main", title="Selaa")
        object Measurements : Screens(route = "browse_measurements", title="Mittaukset") {
            object Main : Screens(route = "browse_measurements_main", title="Mittaukset")
            object Measurement : Screens(route = "browse_measurements_measurement/{id}", title="Mittaus")
        }
        object Injections : Screens(route = "browse_injections", title="Insuliini") {
            object Main : Screens(route = "browse_injections_main", title="Insuliini")
            object Injection : Screens(route = "browse_injections_injection/{id}", title="Insuliini")
        }
        object Meals: Screens(route = "browse_meals", title="Ateriat")
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
    val mealViewModel: MealViewModel = viewModel(
        factory = MealViewModelFactory(repository = repository)
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 8.dp, end = 8.dp)
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
                navigation(
                    route = Screens.Browse.Measurements.route,
                    startDestination = Screens.Browse.Measurements.Main.route
                ) {
                    composable(route = Screens.Browse.Measurements.Main.route) {
                        BrowseMeasurementsScreen(
                            browseViewModel = browseViewModel,
                            navController = navController)
                    }
                    composable(route = Screens.Browse.Measurements.Measurement.route + "/{id}") {
                        MeasurementScreen(
                            measurementViewModel = measurementViewModel,
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
                            browseViewModel = browseViewModel,
                            navController = navController
                        )
                    }
                    composable(route = Screens.Browse.Injections.Injection.route + "/{id}") {
                        InsulinScreen(
                            insulinViewModel = insulinViewModel,
                            navController = navController,
                            id = it.arguments?.getString("id")
                        )
                    }
                }
                composable(route = Screens.Browse.Meals.route) {
                    BrowseMealsScreen(browseViewModel = browseViewModel)
                }
            }
            composable(route = Screens.Measurement.route) {
                MeasurementScreen(
                    measurementViewModel = measurementViewModel,
                    navController = navController
                )
            }
            composable(route = Screens.Insulin.route) {
                InsulinScreen(
                    insulinViewModel = insulinViewModel,
                    navController = navController
                )
            }
            composable(route = Screens.Meal.route) {
                MealScreen(
                    mealViewModel = mealViewModel,
                    navController = navController
                )
            }
            composable(route = Screens.Settings.route) {
                SettingsScreen()
            }
        }
    }
}