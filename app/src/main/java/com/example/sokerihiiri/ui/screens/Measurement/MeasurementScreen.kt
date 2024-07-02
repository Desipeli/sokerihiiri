package com.example.sokerihiiri.ui.screens.Measurement

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.TimePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementScreen(
    measurementViewModel: MeasurementViewModel,
    modifier: Modifier = Modifier,
) {

    // https://medium.com/@droidvikas/exploring-date-and-time-pickers-compose-bytes-120e75349797
    // https://material.io/blog/material-3-compose-1-1

    val uiState = measurementViewModel.bloodSugarMeasurementState

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = uiState.date)
    var showDatePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        is24Hour = true,
        initialHour = uiState.hour,
        initialMinute = uiState.minute)
    var showTimePicker by remember { mutableStateOf(false) }


    fun handleValueChange(newValue: String) {
        try {
            measurementViewModel.setValue(newValue.toFloat())
        } catch (e: NumberFormatException) {
            Log.e("MeasurementScreen", "Invalid value: $newValue")
        }
    }

    fun handleHourChange(newValue: String) {
        try {
            measurementViewModel.setHoursFromMeal(newValue.toInt())
        } catch (e: NumberFormatException) {
            Log.e("MeasurementScreen", "Invalid value: $newValue")
            measurementViewModel.setHoursFromMeal(0)
        }
    }

    fun handleMinutesChange(newValue: String) {
        try {
            measurementViewModel.setMinutesFromMeal(newValue.toInt())
        } catch (e: NumberFormatException) {
            Log.e("MeasurementScreen", "Invalid value: $newValue")
            measurementViewModel.setMinutesFromMeal(0)
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(bottom = 16.dp)) {

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = if (uiState.value <= 0.0f) "" else uiState.value.toString(),
                onValueChange = { handleValueChange(it) },
                label = { Text("Verensokeri mmol/l ") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = {
                        defaultKeyboardAction(ImeAction.Done)
                    }
                )
            )

            Spacer(modifier = Modifier.height(64.dp))

            TextButton(onClick = { showTimePicker = true }) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        uiState.hour,
                        uiState.minute
                    )
                )
            }
            TextButton(onClick = { showDatePicker = true }) {
                Text(dateFormatter.format(Date(uiState.date)))
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Aterian jälkeen")
                Checkbox(checked = uiState.afterMeal, onCheckedChange = {
                    measurementViewModel.setAfterMeal(it)
                } )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(modifier = Modifier
                    .width(64.dp),
                    value = if (uiState.minutesFromMeal < 60) "" else (uiState.minutesFromMeal / 60).toString(),
                    label = {Text("h")},
                    onValueChange = { handleHourChange(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    enabled = uiState.afterMeal
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(modifier = Modifier
                    .width(64.dp),
                    value = if (uiState.minutesFromMeal % 60 == 0) "" else (uiState.minutesFromMeal % 60).toString(),
                    label = {Text("min")},
                    onValueChange = {handleMinutesChange(it) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            defaultKeyboardAction(ImeAction.Done)
                        }
                    ),
                    enabled = uiState.afterMeal,
                )
            }
        }
        TextButton(modifier = Modifier
            .align(Alignment.BottomEnd),
            onClick = {
                measurementViewModel.saveBloodSugarMeasurement()
        }) {
            Text("Tallenna")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        measurementViewModel.setDate(selectedMillis)
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Peruuta")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            text = {
                Column {
                    TimePicker(
                        state = timePickerState,
                        layoutType = TimePickerLayoutType.Vertical,
                    )
                }
            },
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                Button(onClick = {
                    measurementViewModel.setTime(timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            }
        )
    }
}