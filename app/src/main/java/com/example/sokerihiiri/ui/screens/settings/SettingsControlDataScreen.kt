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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.sokerihiiri.R
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
        settingsViewModel.deleteAllRoomData(snackbarHostState, context)
        showRemoveAllRoomDataDialog = false
    }

    fun handleDeleteAllMeasurementsConfirm() {
        settingsViewModel.deleteAllMeasurements(snackbarHostState, context)
        showRemoveMeasurementsDialog = false
    }

    fun handleDeleteAllInsulinInjectionsConfirm() {
        settingsViewModel.deleteAllInsulinInjections(snackbarHostState, context)
        showRemoveInsulinInjectionsDialog = false
    }

    fun handleDeleteAllMealsConfirm() {
        settingsViewModel.deleteAllMeals(snackbarHostState, context)
        showRemoveMealsDialog = false
    }

    fun handleDeleteAllOthersConfirm() {
        settingsViewModel.deleteAllOthers(snackbarHostState, context)
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
        Text(text = stringResource(R.string.import_and_export_data), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { handleWriteCSVButton() }) {
            Text(text = stringResource(R.string.export_data))
        }
        Button(onClick = { handleLoadMeasurementsFromCSV() }) {
            Text(text = stringResource(R.string.import_data))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = stringResource(R.string.delete_data), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            showRemoveAllRoomDataDialog = true
        }) {
            Text(text = stringResource(R.string.delete_all_data))
        }
        Button(onClick = {
            showRemoveMeasurementsDialog = true
        }) {
            Text(text = stringResource(R.string.delete_all_measurements))
        }
        Button(onClick = {
            showRemoveInsulinInjectionsDialog = true
        }) {
            Text(text = stringResource(R.string.delete_all_insulin))
        }
        Button(onClick = {
            showRemoveMealsDialog = true
        }) {
            Text(text = stringResource(R.string.delete_all_meals))
        }
        Button(onClick = {
            showRemoveOthersDialog = true
        }) {
            Text(text = stringResource(R.string.delete_all_others))
        }
    }

    if (showRemoveAllRoomDataDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllRoomData() },
            onDismiss = { showRemoveAllRoomDataDialog = false },
            title = stringResource(R.string.delete_all_data),
            message = stringResource(R.string.delete_all_data_message),
            confirmText = stringResource(R.string.confirm_delete_data)
        )
    }

    if (showRemoveMeasurementsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllMeasurementsConfirm() },
            onDismiss = { showRemoveMeasurementsDialog = false },
            title = stringResource(R.string.delete_all_measurements),
            message = stringResource(R.string.delete_all_measurements_message),
            confirmText = stringResource(R.string.confirm_delete_data))
    }

    if (showRemoveInsulinInjectionsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllInsulinInjectionsConfirm() },
            onDismiss = { showRemoveInsulinInjectionsDialog = false },
            title = stringResource(R.string.delete_all_insulin),
            message = stringResource(R.string.delete_all_insulin_message),
            confirmText = stringResource(R.string.confirm_delete_data))
    }

    if (showRemoveMealsDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllMealsConfirm() },
            onDismiss = { showRemoveMealsDialog = false },
            title = stringResource(R.string.delete_all_meals),
            message = stringResource(R.string.delete_all_meals_message),
            confirmText = stringResource(R.string.confirm_delete_data))
    }

    if (showRemoveOthersDialog) {
        SeriousConfirmDialog(
            onConfirm = { handleDeleteAllOthersConfirm() },
            onDismiss = { showRemoveOthersDialog = false },
            title = stringResource(R.string.delete_all_others),
            message = stringResource(R.string.delete_all_others_message),
            confirmText = stringResource(R.string.confirm_delete_data))
    }

    if (showLoadFileDialog && uiState.loadingFileType != null) {
        SeriousConfirmDialog(
            onConfirm = {
                showLoadFileDialog = false
                settingsViewModel.readFileContent(context, snackbarHostState)
            },
            onDismiss = { showLoadFileDialog = false },
            title = stringResource(R.string.import_data),
            message = uiState.loadingFileReplaceWarning,
            confirmText = stringResource(R.string.yes)
        )
    }
}