package com.example.sokerihiiri.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.navigation.Screens

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
        Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        StyledTextButton(
            onClick = { navController.navigate(Screens.Measurements.Main.route)},
            content = Screens.Measurements.Main.title
        )
        StyledTextButton(
            onClick = { navController.navigate(Screens.Injections.Main.route) },
            content = Screens.Injections.Main.title)

        StyledTextButton(
            onClick = { navController.navigate(Screens.Meals.Main.route) },
            content = Screens.Meals.Main.title)
    }
}