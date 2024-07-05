package com.example.sokerihiiri.ui.screens.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sokerihiiri.Screens

@Composable
fun BrowseScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextButton(onClick = {
            navController.navigate(Screens.Browse.Measurements.route)
        }) {
            Text(text = Screens.Browse.Measurements.title)
        }
        TextButton(onClick = { navController.navigate(Screens.Browse.Injections.route) }) {
            Text(text = Screens.Browse.Injections.title)
        }
        TextButton(onClick = { navController.navigate(Screens.Browse.Meals.route) }) {
            Text(text = Screens.Browse.Meals.title)
        }
    }
}

