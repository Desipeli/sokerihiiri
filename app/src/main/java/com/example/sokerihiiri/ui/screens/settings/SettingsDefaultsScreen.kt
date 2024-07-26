package com.example.sokerihiiri.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.LocalSettingsViewModel
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.SettingsBase

@Composable
fun SettingsDefaultsScreen(
    snackbarHostState: SnackbarHostState
) {
    val settingsViewModel = LocalSettingsViewModel.current

    val uiState = settingsViewModel.uiState

    fun handleInsulinDoseChange(value: String) {
        try {
            settingsViewModel.setDefaultInsulinDose(value.toInt())
        } catch (e: NumberFormatException) {
            settingsViewModel.setDefaultInsulinDose(0)
        }
    }

    fun handleHoursAfterMealChange(value: String) {
        try {
            settingsViewModel.setDefaultHoursAfterMeal(value.toInt())
        } catch (e: NumberFormatException) {
            settingsViewModel.setDefaultHoursAfterMeal(0)
        }
    }

    fun handleMinutesAfterMealChange(value: String) {
        try {
            settingsViewModel.setDefaultMinutesAfterMeal(value.toInt())
        } catch (e: NumberFormatException) {
            settingsViewModel.setDefaultMinutesAfterMeal(0)
        }
    }

    SettingsBase {
        Text("Verensokeri", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Aterian jälkeen")
        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberTextField(
                modifier = Modifier.width(82.dp),
                value = if (uiState.defaultHoursAfterMeal <= 0) "" else uiState.defaultHoursAfterMeal.toString(),
                onValueChange = { handleHoursAfterMealChange(it) } ,
                label = { Text(text = "h") })
            Spacer(modifier = Modifier.width(16.dp))
            NumberTextField(
                modifier = Modifier.width(82.dp),
                value = if (uiState.defaultMinutesAfterMeal <= 0) "" else uiState.defaultMinutesAfterMeal.toString(),
                onValueChange = { handleMinutesAfterMealChange(it) } ,
                label = { Text(text = "min") })
        }

        Text("Insuliini", style = MaterialTheme.typography.headlineSmall)
        NumberTextField(
            modifier = Modifier.width(170.dp),
            value = if (uiState.defaultInsulinDose <= 0) "" else uiState.defaultInsulinDose.toString(),
            onValueChange = { handleInsulinDoseChange(it)},
            label = { Text("Insuliini") })
    }
}