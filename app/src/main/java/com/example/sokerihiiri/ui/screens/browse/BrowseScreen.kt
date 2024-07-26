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
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.navigation.Screens

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
//        StyledTextButton(onClick = {
//            navController.navigate(Screens.Browse.Measurements.route) },
//            content = Screens.Browse.Measurements.title)
//        StyledTextButton(onClick = { navController.navigate(Screens.Browse.Injections.route) },
//            content = Screens.Browse.Injections.title)
//        StyledTextButton(onClick = { navController.navigate(Screens.Browse.Meals.Main.route) },
//            content = Screens.Browse.Meals.title)
    }
}

