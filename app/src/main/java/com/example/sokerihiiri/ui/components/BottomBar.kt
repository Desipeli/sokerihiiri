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
import com.example.sokerihiiri.ui.LocalInsulinViewModel
import com.example.sokerihiiri.ui.LocalMealViewModel
import com.example.sokerihiiri.ui.LocalMeasurementViewModel
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.utils.baseRoute


@Composable
fun BottomBar(
    navController: NavController,
    snackbarHostState: SnackbarHostState) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val measurementViewModel = LocalMeasurementViewModel.current
    val insulinViewModel = LocalInsulinViewModel.current
    val mealViewModel = LocalMealViewModel.current
    val settingsViewModel = LocalSettingsViewModel.current

    val baseRoute = baseRoute(currentRoute)

    Log.d("BottomBar", "Current route: $currentRoute")

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
                    Screens.Measurements.NewMeasurement.route -> CreateNewActionButton(
                        action = {
                            measurementViewModel.saveBloodSugarMeasurement()
                            navController.navigateUp()
                        },
                    )
                    baseRoute(Screens.Measurements.EditMeasurement.route) -> EditActionButton(
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
                    Screens.Injections.NewInjection.route -> CreateNewActionButton(
                        action = {
                            insulinViewModel.saveInsulinInjection()
                            navController.navigateUp()
                        }
                    )
                    baseRoute(Screens.Injections.EditInjection.route) -> EditActionButton(
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
                    Screens.Meals.NewMeal.route -> CreateNewActionButton(
                        action = {
                            mealViewModel.saveMeal()
                            navController.navigateUp()
                                 },
                        )
                    baseRoute(Screens.Meals.EditMeal.route) -> EditActionButton(
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
                    Screens.Measurements.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Measurements.NewMeasurement.route) }
                    )
                    Screens.Injections.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Injections.NewInjection.route) }
                    )
                    Screens.Meals.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Meals.NewMeal.route) }
                    )
                    Screens.Settings.Defaults.route -> CreateNewActionButton(
                        action = { settingsViewModel.saveDefaultsSettings(snackbarHostState) },
                    )
                    Screens.Settings.Notifications.route -> CreateNewActionButton(
                        action = { settingsViewModel.saveInsulinDeadline(snackbarHostState)}
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
    var confirmDialogState by remember { mutableStateOf(false) }

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
                    confirmDialogState = true
                })
        }
    }
    if (confirmDialogState) {
        ConfirmDialog(
            onDismiss = { confirmDialogState = false },
            onConfirm = deleteAction
        )
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