package com.example.sokerihiiri.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

@Composable
fun StyledTextButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    text: String,
) {
    TextButton(
        onClick = { onClick() },
        enabled = enabled) {
        Text(
            text = text,
            style = style)
    }
}