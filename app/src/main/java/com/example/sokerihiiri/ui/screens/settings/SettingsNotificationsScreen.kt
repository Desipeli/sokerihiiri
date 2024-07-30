package com.example.sokerihiiri.ui.screens.settings

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.SettingsBase
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingsNotificationsScreen(
    snackbarHostState: SnackbarHostState
) {
    // Notifikaatioasetusten ikkuna

    var notificationsAllowed by remember { mutableStateOf(false) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Kysytään lupaa notifikaatioihin, jos API version >= 33
        // Vanhemmissa ei onnistu, mutta oletuksena sallittu

        val postNotificationPermission =
            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

        LaunchedEffect(key1 = true) {
            if (!postNotificationPermission.status.isGranted) {
                notificationsAllowed = false
                postNotificationPermission.launchPermissionRequest()
            } else {
                notificationsAllowed = true
            }
        }
    } else {
        notificationsAllowed = true
    }

    if (notificationsAllowed) {
        SettingsNotificationScreenContent(snackbarHostState)
    } else {
        Text(text = stringResource(R.string.notification_permission_not_granted))
    }

}

@Composable
fun SettingsNotificationScreenContent(snackbarHostState: SnackbarHostState) {
    // Notifikaatioasetukset. Näytetään jos lupaa notifikaatioihin on hyväksytty, tai API version < 33
    
    val settingsViewModel = LocalSettingsViewModel.current
    val uiState = settingsViewModel.uiState

    LaunchedEffect(key1 = Unit) {
        settingsViewModel.getInsulinDeadline()
    }

    val showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    SettingsBase {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Insuliini", style = MaterialTheme.typography.headlineSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.insulinNotification,
                    onCheckedChange = { checked ->
                        settingsViewModel.setInsulinNotification(checked)
                    })
                if (uiState.insulinNotification) {
                    Text(text = stringResource(R.string.notifications_in_use))
                } else {
                    Text(text = stringResource(R.string.use_notifications))
                }

            }

            StyledTextButton(
                onClick = { showTimePicker.value = true },
                enabled = uiState.insulinNotification,
                text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    uiState.insulinDeadlineHours,
                    uiState.insulinDeadlineMinutes
                )
            )
            Text(
                text = stringResource(R.string.notifications_explanation)
            )
        }
    }

    if (showTimePicker.value) {
        AppTimePickerDialog(
            showState = showTimePicker,
            initialHour = uiState.insulinDeadlineHours,
            initialMinute = uiState.insulinDeadlineMinutes,
            onTimeSelected = { hour, minutes ->
                settingsViewModel.setInsulinDeadline(hour, minutes)
            }
        )
    }
}