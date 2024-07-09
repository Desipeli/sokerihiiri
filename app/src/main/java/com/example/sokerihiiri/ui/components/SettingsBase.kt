package com.example.sokerihiiri.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsBase(
    modifier: Modifier = Modifier,
    navController: NavController,
    save: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
        TextButton(modifier = Modifier
            .align(Alignment.BottomEnd),
            onClick = {
                try {
                    save()
                } catch (e: Exception) {
                    Log.e("SettingsBase", "save failed", e)
                }
            }) {
            Text("Tallenna")
        }
    }

}