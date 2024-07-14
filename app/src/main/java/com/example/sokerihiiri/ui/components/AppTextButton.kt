package com.example.sokerihiiri.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StyledTextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: String,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        ) {
        Text(text = content, style = MaterialTheme.typography.headlineSmall)
    }
}