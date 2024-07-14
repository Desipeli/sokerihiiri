package com.example.sokerihiiri.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
            onClick = { navController.navigate(Screens.Measurement.route)},
            content = Screens.Measurement.title
        )
        StyledTextButton(
            onClick = { navController.navigate(Screens.Insulin.route) },
            content = Screens.Insulin.title)

        StyledTextButton(
            onClick = { navController.navigate(Screens.Meal.route) },
            content = Screens.Meal.title)
        StyledTextButton(
            onClick = { navController.navigate(Screens.Browse.route) },
            content = Screens.Browse.title)
    }
}