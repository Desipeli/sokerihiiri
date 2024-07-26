package com.example.sokerihiiri.ui.screens.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.SettingsBase
import com.example.sokerihiiri.ui.navigation.Screens

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    SettingsBase {
        Button(
            onClick = {
                navController.navigate(Screens.Settings.Defaults.route) }) {
            Text(text = Screens.Settings.Defaults.title)
        }
        Button(onClick = {
            navController.navigate(Screens.Settings.ControlData.route)
        } ) {
            Text(text = Screens.Settings.ControlData.title)
        }
        Button(onClick = {
            navController.navigate(Screens.Settings.Notifications.route)
        }) {
            Text(text = Screens.Settings.Notifications.title)
        }
    }
}