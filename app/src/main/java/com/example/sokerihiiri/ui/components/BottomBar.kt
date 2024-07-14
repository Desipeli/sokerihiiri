package com.example.sokerihiiri.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sokerihiiri.ui.LocalBrowseViewModel
import com.example.sokerihiiri.ui.LocalInsulinViewModel
import com.example.sokerihiiri.ui.LocalMealViewModel
import com.example.sokerihiiri.ui.LocalMeasurementViewModel
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.ui.screens.insulin.InsulinViewModel
import com.example.sokerihiiri.ui.screens.meal.MealViewModel
import com.example.sokerihiiri.ui.screens.measurement.MeasurementViewModel

fun baseRoute(route: String?): String? {
    return route?.substringBeforeLast("/")
}

@Composable
fun BottomBar(
    navController: NavController,
    snackbarHostState: SnackbarHostState) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val measurementViewModel = LocalMeasurementViewModel.current
    val insulinViewModel = LocalInsulinViewModel.current
    val mealViewModel = LocalMealViewModel.current
    val browserViewModel = LocalBrowseViewModel.current
    val settingsViewModel = LocalSettingsViewModel.current

    var baseRoute = baseRoute(currentRoute)

    BottomAppBar(
        actions = {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Takaisin")
                }
                IconButton(onClick = { navController.navigate(Screens.Main.route) }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Päävalikko")
                }
                when(baseRoute) {
                    Screens.Measurement.route -> CreateNewActionButton(
                        action = {
                            measurementViewModel.saveBloodSugarMeasurement()
                            navController.navigateUp()
                        },
                    )
                    baseRoute(Screens.Browse.Measurements.Measurement.route) -> EditActionButton(
                        updateAction = {
                            measurementViewModel.updateBloodSugarMeasurement()
                            navController.navigateUp()
                        },
                        deleteAction = {
                            measurementViewModel.deleteBloodSugarMeasurement()
                            navController.navigateUp()
                        },
                        canEdit = measurementViewModel.uiState.canEdit,
                        setCanEdit = { measurementViewModel.setCanEdit(it) }
                    )
                    Screens.Insulin.route -> CreateNewActionButton(
                        action = {
                            insulinViewModel.saveInsulinInjection()
                            navController.navigateUp()
                        }
                    )
                    baseRoute(Screens.Browse.Injections.Injection.route) -> EditActionButton(
                        updateAction = {
                            insulinViewModel.updateInsulinInjection()
                            navController.navigateUp()
                        },
                        deleteAction = {
                            insulinViewModel.deleteInsulinInjectionById()
                            navController.navigateUp()
                        },
                        canEdit = insulinViewModel.uiState.canEdit,
                        setCanEdit = { insulinViewModel.setCanEdit(it) }
                    )
                    Screens.Meal.route -> CreateNewActionButton(
                        action = {
                            mealViewModel.saveMeal()
                            navController.navigateUp()
                                 },
                        )
                    baseRoute(Screens.Browse.Meals.Meal.route) -> EditActionButton(
                        updateAction = {
                            mealViewModel.updateMeal()
                            navController.navigateUp()
                        },
                        deleteAction = {
                            mealViewModel.deleteMealById()
                            navController.navigateUp()
                        },
                        canEdit = mealViewModel.uiState.canEdit,
                        setCanEdit = { mealViewModel.setCanEdit(it) }
                    )
                    Screens.Browse.Measurements.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Measurement.route) }
                    )
                    Screens.Browse.Injections.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Insulin.route) }
                    )
                    Screens.Browse.Meals.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Meal.route) }
                    )
                    Screens.Settings.Defaults.route -> CreateNewActionButton(
                        action = { settingsViewModel.saveDefaultsSettings(snackbarHostState) },
                    )
                    else -> { IconButton(onClick = {}, enabled = false) {}
                    }
                }
            }
        }
    )
}

@Composable
fun EditActionButton(
    updateAction: () -> Unit,
    deleteAction: () -> Unit,
    canEdit: Boolean,
    setCanEdit: (Boolean) -> Unit) {
    var expandend by remember { mutableStateOf(false) }

    Box {
        if (canEdit) {
            IconButton(
                onClick = {
                    try {
                        updateAction()
                    } catch (e: Exception) {
                        Log.e("BottomBar", "Error saving insulin injection: ${e.message}")
                    }
                })
            {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "Päivitä")
            }
        } else {
            IconButton(
                onClick = {
                    expandend = true
                })
            {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "Muokkaa")
            }
        }

        DropdownMenu(
            expanded = expandend,
            onDismissRequest = { expandend = false }) {
            DropdownMenuItem(
                text = { Text("Muokkaa") },
                onClick = {
                    setCanEdit(true)
                    expandend = false
                })
            DropdownMenuItem(
                text = { Text("Poista") },
                onClick = {
                    deleteAction()
                })
        }
    }
}

@Composable
fun CreateNewActionButton(
    action: () -> Unit = {},
) {
    IconButton(
        onClick = {
            try {
                action()
            } catch (e: Exception) {
                Log.e("BottomBar", "${e.message}")
            }
        })
    {
        Icon(imageVector = Icons.Filled.Check, contentDescription = "Tallenna")
    }
}

@Composable
fun BrowseCreateNewActionButton(
    action: () -> Unit = {}
) {
    IconButton(
        onClick = {
            action()
        })
    {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Kirjaa uusi")
    }
}