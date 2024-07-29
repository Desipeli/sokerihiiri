package com.example.sokerihiiri.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.sokerihiiri.R

@Composable
fun SeriousConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String = "",
    message: String = "",
    confirmText: String = "",
) {
    var checked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val checkboxColors = CheckboxDefaults.colors(
        checkedColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        uncheckedColor = if (isError) MaterialTheme.colorScheme.error.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        checkmarkColor = MaterialTheme.colorScheme.onPrimary
    )

    LaunchedEffect(checked) {
        if (checked) {
            isError = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            Column {
                Text(text = message)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it },
                        colors = checkboxColors)
                    Text(text = confirmText)
                    }
            }},
        confirmButton = {
            TextButton(
                onClick = {
                    if (checked) {
                        try {
                            onConfirm()
                        } catch (e: Exception) {
                            Log.e("RemoveDataDialog", "${e.message}")
                        }
                    } else {
                        isError = true
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}