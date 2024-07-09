package com.example.sokerihiiri.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.SettingsBase

@Composable
fun SettingsDefaultsScreen(
    navController: NavController,
) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()

    val uiState = settingsViewModel.uiState

    fun handleInsulinDoseChange(value: String) {
        try {
            val newDose = value.toInt()
            settingsViewModel.setDefaultInsulinDose(newDose)
        } catch (e: NumberFormatException) {
            settingsViewModel.setDefaultInsulinDose(0)
        }
    }

    fun saveSettings() {
        settingsViewModel.saveSettings()
    }

    SettingsBase(
        navController = navController,
        save = { saveSettings() }) {
        Text("Oletusarvot", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Verensokeri", style = MaterialTheme.typography.headlineSmall)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NumberTextField(
                modifier = Modifier.width(82.dp),
                value = "0",
                onValueChange = {} ,
                label = { Text(text = "h") })
            Spacer(modifier = Modifier.width(16.dp))
            NumberTextField(
                modifier = Modifier.width(82.dp),
                value = "0",
                onValueChange = {} ,
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