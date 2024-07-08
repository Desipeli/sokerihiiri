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
import androidx.navigation.compose.rememberNavController
import com.example.sokerihiiri.repository.SokerihiiriDatabase
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.ui.components.BottomBar
import com.example.sokerihiiri.ui.screens.measurement.MeasurementViewModel
import com.example.sokerihiiri.ui.screens.measurement.MeasurementViewModelFactory
import com.example.sokerihiiri.ui.components.TopBar
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.ui.navigation.browseNavigation
import com.example.sokerihiiri.ui.screens.insulin.InsulinScreen
import com.example.sokerihiiri.ui.screens.insulin.InsulinViewModel
import com.example.sokerihiiri.ui.screens.insulin.InsulinViewModelFactory
import com.example.sokerihiiri.ui.screens.MainScreen
import com.example.sokerihiiri.ui.screens.meal.MealScreen
import com.example.sokerihiiri.ui.screens.measurement.MeasurementScreen
import com.example.sokerihiiri.ui.screens.SettingsScreen
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModel
import com.example.sokerihiiri.ui.screens.browse.BrowseViewModelFactory
import com.example.sokerihiiri.ui.screens.meal.MealViewModel
import com.example.sokerihiiri.ui.screens.meal.MealViewModelFactory



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
            browseNavigation(
                navController = navController,
                browseViewModel = browseViewModel,
                measurementViewModel = measurementViewModel,
                insulinViewModel = insulinViewModel,
                mealViewModel = mealViewModel
            )

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