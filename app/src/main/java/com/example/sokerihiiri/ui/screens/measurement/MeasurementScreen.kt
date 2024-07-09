package com.example.sokerihiiri.ui.screens.measurement

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.ViewAndEdit
import java.util.Date

@Composable
fun MeasurementScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    id: String? = null,
) {
    val measurementViewModel: MeasurementViewModel = hiltViewModel()
    var allowEdit: MutableState<Boolean> = remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        try {
            if (id == null) allowEdit.value = true
            measurementViewModel.getMeasurementFromId(id)
        } catch (e: Exception) {
            Log.e("MeasurementScreen", "Error getting measurement: $e")
        }
    }

    // https://medium.com/@droidvikas/exploring-date-and-time-pickers-compose-bytes-120e75349797
    // https://material.io/blog/material-3-compose-1-1

    val uiState = measurementViewModel.uiState

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    var showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    Log.d("MeasurementScreen", "uiState: $uiState")


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

    ViewAndEdit(
        navController = navController,
        id = id,
        allowEdit = allowEdit,
        save = { measurementViewModel.saveBloodSugarMeasurement() },
        update = { measurementViewModel.updateBloodSugarMeasurement() },
        delete = { measurementViewModel.deleteBloodSugarMeasurement() }) {

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = if (uiState.value <= 0.0f) "" else uiState.value.toString(),
                onValueChange = { handleValueChange(it) },
                label = { Text("Verensokeri mmol/l ") },
                enabled = allowEdit.value,
                isError = uiState.valueError != null
            )

            Spacer(modifier = Modifier.height(64.dp))

            TextButton(
                onClick = { showTimePicker.value = true },
                enabled = allowEdit.value
            ) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        uiState.hour,
                        uiState.minute
                    )
                )
            }
            TextButton(
                onClick = { showDatePicker.value = true },
                enabled = allowEdit.value
            ) {
                Text(dateFormatter.format(Date(uiState.date)))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Aterian jälkeen")
                Checkbox(
                    checked = uiState.afterMeal,
                    enabled = allowEdit.value,
                    onCheckedChange = {
                        measurementViewModel.setAfterMeal(it)
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberTextField(
                    modifier = Modifier
                        .width(64.dp),
                    value = if (uiState.minutesFromMeal < 60) "" else (uiState.minutesFromMeal / 60).toString(),
                    onValueChange = { handleHourChange(it) },
                    label = { Text("h") },
                    enabled = allowEdit.value && uiState.afterMeal
                )
                Spacer(modifier = Modifier.width(8.dp))
                NumberTextField(
                    modifier = Modifier
                        .width(64.dp),
                    value = if (uiState.minutesFromMeal % 60 == 0) "" else (uiState.minutesFromMeal % 60).toString(),
                    onValueChange = { handleMinutesChange(it) },
                    label = { Text("min") },
                    enabled = allowEdit.value && uiState.afterMeal
                )
            }
        }
    }

    if (showDatePicker.value) {
        AppDatePickerDialog(
            showState = showDatePicker,
            initialState = uiState.date,
            onDateSelected = { measurementViewModel.setDate(it) })
    }

    if (showTimePicker.value) {
        AppTimePickerDialog(
            showState = showTimePicker,
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onTimeSelected = { hour, minute ->
                measurementViewModel.setTime(hour, minute)
            }
        )
    }
}