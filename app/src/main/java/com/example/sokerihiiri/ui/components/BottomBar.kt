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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.LocalInsulinViewModel
import com.example.sokerihiiri.ui.LocalMealViewModel
import com.example.sokerihiiri.ui.LocalMeasurementViewModel
import com.example.sokerihiiri.ui.LocalOtherViewModel
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.utils.baseRoute


@Composable
fun BottomBar(
    navController: NavController,
    snackbarHostState: SnackbarHostState) {

    /* Sovelluksessa näkyvä alapalkki.
    Oikeassa laidassa oleva toimintonappi vaihtuu reitin mukaan.
    Id:n sisältävistä reiteistä on poistettava id-osa, jotta oikean napin luominen onnistuu
    Toimintonapit vaativat usein saman ViewModelin kuin varsinainen ruutu.
     */

    val context = LocalContext.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val measurementViewModel = LocalMeasurementViewModel.current
    val insulinViewModel = LocalInsulinViewModel.current
    val mealViewModel = LocalMealViewModel.current
    val settingsViewModel = LocalSettingsViewModel.current
    val otherViewModel = LocalOtherViewModel.current

    val baseRoute = baseRoute(currentRoute)

    Log.d("BottomBar", "Current route: $currentRoute")
    Log.d("BottomBar", "Base route: $baseRoute")

    BottomAppBar(
        actions = {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
                IconButton(onClick = { navController.navigate(Screens.Main.route) }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = stringResource(R.string.menu)
                    )
                }

                when(baseRoute) {
                    Screens.Measurements.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Measurements.NewMeasurement.route) }
                    )
                    Screens.Measurements.NewMeasurement.route -> CreateNewActionButton(
                        action = {
                            measurementViewModel.saveBloodSugarMeasurement(context)
                            navController.navigateUp()
                        },
                    )
                    baseRoute(Screens.Measurements.EditMeasurement.route) -> EditActionButton(
                        updateAction = {
                            measurementViewModel.updateBloodSugarMeasurement(context)
                            navController.navigateUp()
                        },
                        deleteAction = {
                            measurementViewModel.deleteBloodSugarMeasurement()
                            navController.navigateUp()
                        },
                        canEdit = measurementViewModel.uiState.canEdit,
                        setCanEdit = { measurementViewModel.setCanEdit(it) }
                    )

                    Screens.Injections.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Injections.NewInjection.route) }
                    )
                    Screens.Injections.NewInjection.route -> CreateNewActionButton(
                        action = {
                            insulinViewModel.saveInsulinInjection(context)
                            navController.navigateUp()
                        }
                    )
                    baseRoute(Screens.Injections.EditInjection.route) -> EditActionButton(
                        updateAction = {
                            insulinViewModel.updateInsulinInjection(context)
                            navController.navigateUp()
                        },
                        deleteAction = {
                            insulinViewModel.deleteInsulinInjectionById()
                            navController.navigateUp()
                        },
                        canEdit = insulinViewModel.uiState.canEdit,
                        setCanEdit = { insulinViewModel.setCanEdit(it) }
                    )

                    Screens.Meals.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Meals.NewMeal.route) }
                    )
                    Screens.Meals.NewMeal.route -> CreateNewActionButton(
                        action = {
                            mealViewModel.saveMeal(context)
                            navController.navigateUp()
                                 },
                        )
                    baseRoute(Screens.Meals.EditMeal.route) -> EditActionButton(
                        updateAction = {
                            mealViewModel.updateMeal(context)
                            navController.navigateUp()
                        },
                        deleteAction = {
                            mealViewModel.deleteMealById()
                            navController.navigateUp()
                        },
                        canEdit = mealViewModel.uiState.canEdit,
                        setCanEdit = { mealViewModel.setCanEdit(it) }
                    )

                    Screens.Others.Main.route -> BrowseCreateNewActionButton(
                        action = { navController.navigate(Screens.Others.NewOther.route) }
                    )
                    Screens.Others.NewOther.route -> CreateNewActionButton(
                        action = {
                            otherViewModel.saveOther(context)
                            navController.navigateUp()
                        }
                    )
                    Screens.Others.EditOther.route -> EditActionButton(
                        updateAction = {
                            otherViewModel.updateOther(context)
                            navController.navigateUp()
                        },
                        deleteAction = {
                            otherViewModel.deleteOther()
                            navController.navigateUp()
                        },
                        canEdit = otherViewModel.uiState.canEdit,
                        setCanEdit = { otherViewModel.setCanEdit(it) }
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

    // Muokkausruudussa oleva toimintonappi

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
                Icon(imageVector = Icons.Filled.Check, contentDescription = stringResource(R.string.update))
            }
        } else {
            IconButton(
                onClick = {
                    expandend = true
                })
            {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = stringResource(R.string.edit))
            }
        }

        DropdownMenu(
            expanded = expandend,
            onDismissRequest = { expandend = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = {
                    setCanEdit(true)
                    expandend = false
                })
            DropdownMenuItem(
                text = { Text(stringResource(R.string.remove)) },
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
    // Nappi uuden tapahtuman luomiseen
    IconButton(
        onClick = {
            try {
                action()
            } catch (e: Exception) {
                Log.e("BottomBar", "${e.message}")
            }
        })
    {
        Icon(imageVector = Icons.Filled.Check, contentDescription = stringResource(R.string.save))
    }
}

@Composable
fun BrowseCreateNewActionButton(
    action: () -> Unit = {}
) {
    // Nappi, jota painamalla avataan ruutu uuden tapahtuman luomiseen.
    IconButton(
        onClick = {
            action()
        })
    {
        Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.book_new))
    }
}