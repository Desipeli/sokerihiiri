package com.example.sokerihiiri.ui.screens.settings

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.components.SeriousConfirmDialog
import com.example.sokerihiiri.ui.components.SettingsBase
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsControlDataScreen(
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val settingsViewModel = LocalSettingsViewModel.current
    val uiState = settingsViewModel.uiState

    var showRemoveAllRoomDataDialog by remember { mutableStateOf(false) }
    var showRemoveMeasurementsDialog by remember { mutableStateOf(false) }
    var showRemoveInsulinInjectionsDialog by remember { mutableStateOf(false) }
    var showRemoveMealsDialog by remember { mutableStateOf(false) }
    var showRemoveOthersDialog by remember { mutableStateOf(false) }
    var showLoadFileDialog by remember { mutableStateOf(false) }

    val writeCSVDirLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree())
    {  uri: Uri? ->
        if (uri != null) {
            Log.d("SettingsControlDataScreen", "SettingsControlDataScreen: $uri")
            settingsViewModel.startWriteToCSV(context, uri, snackbarHostState)

        }
    }
    val readCSVLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument())
    {  uri: Uri? ->
        if (uri != null) {
            settingsViewModel.setLoadingFileUri(uri)
            val documentFile = DocumentFile.fromSingleUri(context, uri)
            val fileName = documentFile?.name
            if (fileName != null) {
                settingsViewModel.setLoadingFileName(fileName)
                settingsViewModel.checkFileContent(context, uri, snackbarHostState)
                showLoadFileDialog = true
            }
        }
    }

    val writePermissionState = rememberPermissionState(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val readPermissionState = rememberPermissionState(
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun handleDeleteAllRoomData() {
        settingsViewModel.deleteAllRoomData(snackbarHostState)
        showRemoveAllRoomDataDialog = false
    }

    fun handleDeleteAllMeasurementsConfirm() {
        settingsViewModel.deleteAllMeasurements(snackbarHostState)
        showRemoveMeasurementsDialog = false
    }

    fun handleDeleteAllInsulinInjectionsConfirm() {
        settingsViewModel.deleteAllInsulinInjections(snackbarHostState)
        showRemoveInsulinInjectionsDialog = false
    }

    fun handleDeleteAllMealsConfirm() {
        settingsViewModel.deleteAllMeals(snackbarHostState)
        showRemoveMealsDialog = false
    }

    fun handleDeleteAllOthersConfirm() {
        settingsViewModel.deleteAllOthers(snackbarHostState)
        showRemoveOthersDialog = false
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            readCSVLauncher.launch(arrayOf("*/*"))
        } else {
            // Kysyttävä lupaa jos api level 28 tai alle.
            if (readPermissionState.status.isGranted) {
                Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission granted")
                readCSVLauncher.launch(arrayOf("*/*"))
            } else {
                readPermissionState.launchPermissionRequest()
                if (readPermissionState.status.shouldShowRationale) {
                    Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission denied, show rationale")
                } else {
                    Log.d("SettingsControlDataScreen", "handleWriteCSV: Permission denied")
                }
            }
        }
    }

    SettingsBase {
        Text(text = "Tietojen tallennus ja lataus", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { handleWriteCSVButton() }) {
            Text(text = "Tallenna tiedot csv-tiedostoihin")
        }
        Button(onClick = { handleLoadMeasurementsFromCSV() }) {
            Text(text = "Lataa tietoja csv-tiedostosta")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Tietojen poisto", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

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
        Button(onClick = {
            showRemoveOthersDialog = true
        }) {
            Text(text = "Poista kaikki muut tiedot")
        }
    }

    if (showRemoveAllRoomDataDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllRoomData() },
            onDismiss = { showRemoveAllRoomDataDialog = false },
            title = "Poista kaikki tiedot",
            message = "Haluatko varmasti poistaa kaikki tiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveMeasurementsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllMeasurementsConfirm() },
            onDismiss = { showRemoveMeasurementsDialog = false },
            title = "Poista kaikki mittaustiedot",
            message = "Haluatko varmasti poistaa kaikki mittaustiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveInsulinInjectionsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllInsulinInjectionsConfirm() },
            onDismiss = { showRemoveInsulinInjectionsDialog = false },
            title = "Poista kaikki insuliinikirjaukset",
            message = "Haluatko varmasti poistaa kaikki insuliinikirjaukset? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveMealsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllMealsConfirm() },
            onDismiss = { showRemoveMealsDialog = false },
            title = "Poista kaikki ateriatiedot",
            message = "Haluatko varmasti poistaa kaikki ateriatiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showRemoveOthersDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllOthersConfirm() },
            onDismiss = { showRemoveOthersDialog = false },
            title = "Poista kaikki muut tiedot",
            message = "Haluatko varmasti poistaa kaikki muut tiedot? Tietoja ei voi palauttaa poistamisen jälkeen.",
            confirmText = "Ymmärrän ja haluan poistaa tiedot.")
    }

    if (showLoadFileDialog && uiState.loadingFileType != null) {
        SeriousConfirmDialog(
            onConfirm = {
                showLoadFileDialog = false
                settingsViewModel.readFileContent(context, snackbarHostState)
            },
            onDismiss = { showLoadFileDialog = false },
            title = "Lataa tietoja csv-tiedostosta",
            message = uiState.loadingFileReplaceWarning,
            confirmText = "Kyllä"
        )
    }
}