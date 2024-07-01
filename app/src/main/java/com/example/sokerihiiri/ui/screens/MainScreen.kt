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
import com.example.sokerihiiri.Routes

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Button(onClick = { navController.navigate(Routes.Measurement.name) }) {
            Text(text = Routes.Measurement.title)
        }
        Button(onClick = { navController.navigate(Routes.Insulin.name) }) {
            Text(text = Routes.Insulin.title)
        }
        Button(onClick = { navController.navigate(Routes.Meal.name) }) {
            Text(text = Routes.Meal.title)
        }
        Button(onClick = { navController.navigate(Routes.Browse.name) }) {
            Text(text = Routes.Browse.title)
        }
        Button(onClick = { navController.navigate(Routes.Settings.name) }) {
            Text(text = Routes.Settings.title)
        }
    }
}