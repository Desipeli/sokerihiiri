package com.example.sokerihiiri.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sokerihiiri.Routes

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        actions = {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "")
                }
                IconButton(onClick = { navController.navigate(Routes.Main.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "")
                }
                IconButton(onClick = { navController.navigate(Routes.Settings.name) }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "")
                }
            }

        }
    )
}