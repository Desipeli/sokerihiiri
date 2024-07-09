package com.example.sokerihiiri.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sokerihiiri.ui.components.BottomBar
import com.example.sokerihiiri.ui.components.TopBar
import com.example.sokerihiiri.ui.navigation.AppNavHost



@Composable
fun AppContent(
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
        topBar = {
            TopBar(
                title = { Text(text = "Sokerihiiri") }
            )
        }) { innerPadding ->
        AppNavHost(navController, innerPadding)
    }
}