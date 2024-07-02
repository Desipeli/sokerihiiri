package com.example.sokerihiiri.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    showState: MutableState<Boolean>,
    initialState: Long = System.currentTimeMillis(),
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit = { showState.value = false },
    confirmButtonText: String = "OK",
    dismissButtonText: String = "Peruuta"
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialState)


    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                showState.value = false
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    onDateSelected(selectedMillis)
                }
            }) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { showState.value = false }) {
                Text(dismissButtonText)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    showState: MutableState<Boolean>,
    initialHour: Int = LocalTime.now().hour,
    initialMinute: Int = LocalTime.now().minute,
    onTimeSelected: (Int, Int) -> Unit,
    confirmButtonText: String = "OK",
) {
    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = initialHour,
        initialMinute = initialMinute)

    AlertDialog(
        text = {
            Column {
                TimePicker(
                    state = timePickerState,
                    layoutType = TimePickerLayoutType.Vertical,
                )
            }
        },
        onDismissRequest = { showState.value = false },
        confirmButton = {
            Button(onClick = {
                showState.value = false
                onTimeSelected(
                    timePickerState.hour,
                    timePickerState.minute
                )
            }) {
                Text(confirmButtonText)
            }
        }
    )
}