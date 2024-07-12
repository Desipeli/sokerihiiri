package com.example.sokerihiiri.ui.screens.settings

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.RemoveDataDialog
import com.example.sokerihiiri.ui.components.SettingsBase
import com.example.sokerihiiri.ui.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsControlDataScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val writeCSVDirLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree())
    {  uri: Uri? ->
        if (uri != null) {
            Log.d("SettingsControlDataScreen", "SettingsControlDataScreen: $uri")
            settingsViewModel.startWriteToCSV(context, uri, snackbarHostState)

        }
    }
    val writePermissionState = rememberPermissionState(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val readPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

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

    fun handleWriteCSVButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeCSVDirLauncher.launch(null)
        } else {
            // Kysyttävä lupaa jos api level 28 tai alle.
            if (writePermissionState.status.isGranted) {
                Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission granted")
                writeCSVDirLauncher.launch(null)
            } else {
                writePermissionState.launchPermissionRequest()
                if (writePermissionState.status.shouldShowRationale) {
                    Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission denied, show rationale")
                } else {
                    Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission denied")
                }
            }
        }
    }

    fun handleLoadMeasurementsFromCSV() {

    }

    SettingsBase(
        navController = navController,
        parentScreen = Screens.Settings.Main,
    ) {
        Button(onClick = { handleWriteCSVButton() }) {
            Text(text = "Tallenna tiedot csv-tiedostoihin")
        }
        Button(onClick = {handleLoadMeasurementsFromCSV()}) {
            Text(text = "Lataa mittaustiedot tiedostosta")
        }
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