package com.example.sokerihiiri.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sokerihiiri.ui.navigation.Screens
import com.example.sokerihiiri.ui.navigation.screenList
import com.example.sokerihiiri.utils.baseRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val baseRoute = baseRoute(currentRoute)

    val currentScreen = screenList.find { it.route == baseRoute }
    val currentLogo = currentScreen?.logo

    Log.d("TopBar", "currentRoute: $currentRoute")
    Log.d("TopBar", "currentScreen: $currentScreen")

    CenterAlignedTopAppBar(
        title = { Text(text = currentScreen?.title ?: "Sokerihiiri") },
        modifier = modifier,
        navigationIcon = {
            if (currentLogo != null) {
                Image(
                    painter = painterResource(id = currentLogo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )
            }

        },
        actions = {
            IconButton(onClick = {
                val settingsRoute = when (currentRoute) {
                    Screens.Measurements.EditMeasurement.route -> Screens.Settings.Defaults.route
                    Screens.Injections.EditInjection.route -> Screens.Settings.Defaults.route
                    Screens.Meals.EditMeal.route -> Screens.Settings.Defaults.route
                    else -> Screens.Settings.Main.route
                }
                navController.navigate(settingsRoute)
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "")
            }
        }
    )
}