package com.example.sokerihiiri.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sokerihiiri.ui.navigation.screenList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val currentScreen = screenList.find { it.route == currentRoute }

    CenterAlignedTopAppBar(
        title = { Text(text = currentScreen?.title ?: "Sokerihiiri") },
        modifier = modifier
    )
}