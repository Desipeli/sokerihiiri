package com.example.sokerihiiri.ui.screens.measurements.measurement

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.LocalMeasurementViewModel
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.components.StyledTextField

@Composable
fun MeasurementScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    id: String? = null,
) {
    val measurementViewModel = LocalMeasurementViewModel.current
    val uiState = measurementViewModel.uiState

    LaunchedEffect(Unit) {
        measurementViewModel.resetState()
    }

    try {
        if (id == null) measurementViewModel.setCanEdit(true)
        measurementViewModel.getMeasurementFromId(id)
    } catch (e: Exception) {
        Log.e("MeasurementScreen", "Error getting measurement: $e")
    }

    // https://medium.com/@droidvikas/exploring-date-and-time-pickers-compose-bytes-120e75349797
    // https://material.io/blog/material-3-compose-1-1


    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

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
            label = { Text(stringResource(R.string.blood_sugar_mmol)) },
            enabled = uiState.canEdit,
            isError = uiState.valueError != null
        )

        Spacer(modifier = Modifier.height(64.dp))

        StyledTextButton(
            onClick = { showTimePicker.value = true },
            enabled = uiState.canEdit,
            text = String.format(
                Locale.getDefault(),
                "%02d:%02d",
                uiState.hour,
                uiState.minute
            )
        )

        StyledTextButton(
            onClick = { showDatePicker.value = true },
            enabled = uiState.canEdit,
            text = dateFormatter.format(uiState.date)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.after_meal))
            Checkbox(
                checked = uiState.afterMeal,
                enabled = uiState.canEdit,
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
                label = { Text(stringResource(R.string.h)) },
                enabled = uiState.canEdit && uiState.afterMeal
            )
            Spacer(modifier = Modifier.width(8.dp))
            NumberTextField(
                modifier = Modifier
                    .width(64.dp),
                value = if (uiState.minutesFromMeal % 60 == 0) "" else (uiState.minutesFromMeal % 60).toString(),
                onValueChange = { handleMinutesChange(it) },
                label = { Text(stringResource(R.string.min)) },
                enabled = uiState.canEdit && uiState.afterMeal
            )
        }
        StyledTextField(
            value = uiState.comment,
            onValueChange = { measurementViewModel.setComment(it)},
            enabled = uiState.canEdit,
            label = { Text(stringResource(R.string.comment)) })
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