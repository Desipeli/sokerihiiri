package com.example.sokerihiiri.ui.components

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "Tietojen poisto",
    message: String = "Halautko varmasti poistaa kirjauksen?",
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = {
                try {
                    onConfirm()
                } catch (e: Exception) {
                    Log.e("ConfirmDialog", "Error confirming dialog", e)
                }
            }) {
                Text(text = "Kyllä")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Peruuta")
            }
        }
    )
}