package com.example.sokerihiiri.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sokerihiiri.Screens

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Button(onClick = { navController.navigate(Screens.Measurement.route) }) {
            Text(text = Screens.Measurement.title)
        }
        Button(onClick = { navController.navigate(Screens.Insulin.route) }) {
            Text(text = Screens.Insulin.title)
        }
        Button(onClick = { navController.navigate(Screens.Meal.route) }) {
            Text(text = Screens.Meal.title)
        }
        Button(onClick = { navController.navigate(Screens.Browse.route) }) {
            Text(text = Screens.Browse.title)
        }
    }
}