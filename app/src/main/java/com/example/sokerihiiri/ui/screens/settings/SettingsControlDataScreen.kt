package com.example.sokerihiiri.ui.screens.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.RemoveDataDialog
import com.example.sokerihiiri.ui.components.SettingsBase
import com.example.sokerihiiri.ui.navigation.Screens


@Composable
fun SettingsControlDataScreen(
    navController: NavController,
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    var showRemoveAllRoomDataDialog by remember { mutableStateOf(false) }
    var showRemoveMeasurementsDialog by remember { mutableStateOf(false) }
    var showRemoveInsulinInjectionsDialog by remember { mutableStateOf(false) }
    var showRemoveMealsDialog by remember { mutableStateOf(false) }

    fun handledeleteAllRoomData() {
        settingsViewModel.deleteAllRoomData()
        showRemoveAllRoomDataDialog = false
    }

    fun handleDeleteAllMeasurementsConfirm() {
        settingsViewModel.deleteAllMeasurements()
        showRemoveMeasurementsDialog = false
    }

    fun handleDeleteAllInsulinInjectionsConfirm() {
        settingsViewModel.deleteAllInsulinInjections()
        showRemoveInsulinInjectionsDialog = false
    }

    fun handleDeleteAllMealsConfirm() {
        settingsViewModel.deleteAllMeals()
        showRemoveMealsDialog = false
    }

    SettingsBase(
        navController = navController,
        parentScreen = Screens.Settings.Main,
    ) {
        Button(onClick = {
            showRemoveAllRoomDataDialog = true
        }) {
            Text(text = "Poista kaikki tiedot")
        }
        Button(onClick = {
            showRemoveMeasurementsDialog = true
        }) {
            Text(text = "Poista kaikki mittaukset")
        }
        Button(onClick = {
            showRemoveInsulinInjectionsDialog = true
        }) {
            Text(text = "Poista kaikki insuliinikirjaukset")
        }
        Button(onClick = {
            showRemoveMealsDialog = true
        }) {
            Text(text = "Poista kaikki ateriatiedot")
        }
    }

    if (showRemoveAllRoomDataDialog) {
        RemoveDataDialog(
            onConfirm = { handledeleteAllRoomData() },
            onDismiss = { showRemoveAllRoomDataDialog = false },
            title = "Poista kaikki tiedot",
            message = "Haluatko varmasti poistaa kaikki tiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveMeasurementsDialog) {
        RemoveDataDialog(
            onConfirm = { handleDeleteAllMeasurementsConfirm() },
            onDismiss = { showRemoveMeasurementsDialog = false },
            title = "Poista kaikki mittaustiedot",
            message = "Haluatko varmasti poistaa kaikki mittaustiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveInsulinInjectionsDialog) {
        RemoveDataDialog(
            onConfirm = { handleDeleteAllInsulinInjectionsConfirm() },
            onDismiss = { showRemoveInsulinInjectionsDialog = false },
            title = "Poista kaikki insuliinikirjaukset",
            message = "Haluatko varmasti poistaa kaikki insuliinikirjaukset? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveMealsDialog) {
        RemoveDataDialog(
            onConfirm = { handleDeleteAllMealsConfirm() },
            onDismiss = { showRemoveMealsDialog = false },
            title = "Poista kaikki ateriatiedot",
            message = "Haluatko varmasti poistaa kaikki ateriatiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }
}