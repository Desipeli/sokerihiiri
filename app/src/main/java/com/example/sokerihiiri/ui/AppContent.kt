package com.example.sokerihiiri.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) },
        topBar = {
            TopBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        AppNavHost(navController, snackbarHostState, innerPadding)
    }
}