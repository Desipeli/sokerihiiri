package com.example.sokerihiiri.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sokerihiiri.ui.AppViewModel

@Composable
fun MeasurementScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(text = "Mittaus")
    }
}