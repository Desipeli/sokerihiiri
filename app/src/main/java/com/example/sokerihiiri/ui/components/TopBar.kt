package com.example.sokerihiiri.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {}) {
    CenterAlignedTopAppBar(
        title = title,
        modifier = modifier
    )
}