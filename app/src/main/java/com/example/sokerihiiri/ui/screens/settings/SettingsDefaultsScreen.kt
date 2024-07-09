package com.example.sokerihiiri.ui.screens.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        settingsViewModel.getDefaultInsulinDose(context)
    }
    SettingsBase(navController = navController) {
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
            value = "0" ,
            onValueChange = {},
            label = { Text("Insuliini") })
    }
}