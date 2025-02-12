package com.example.sokerihiiri.ui.screens.injections.insulin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sokerihiiri.R
import com.example.sokerihiiri.ui.LocalInsulinViewModel
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.components.StyledTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InsulinScreen(
    id: String? = null
) {
    val insulinViewModel = LocalInsulinViewModel.current
    val uiState = insulinViewModel.uiState


    LaunchedEffect(Unit) {
        try {
            if (id == null) {
                insulinViewModel.resetState()
                insulinViewModel.setCanEdit(true)
            } else {
                insulinViewModel.getInsulinInjectionById(id)
            }
        } catch (e: Exception) {
            Log.e("InsulinScreen", "Failed to get measurement", e)
        }
    }


    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    fun handleDoseChange(newDose: String) {
        try {
            insulinViewModel.setDose(newDose.toInt())
        } catch (e: Exception) {
            Log.e("InsulinScreen", "Failed to parse dose", e)
            insulinViewModel.setDose(0)
        }
    }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NumberTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = if (uiState.dose <= 0) "" else uiState.dose.toString(),
                onValueChange = {handleDoseChange(it)},
                label = { Text(stringResource(R.string.insulin_dose_label)) },
                enabled = uiState.canEdit,
                isError = uiState.doseError != null)
            uiState.doseError?.let { Text(text = it) }
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
                text = dateFormatter.format(Date(uiState.date)
                )
            )
            StyledTextField(
                value = uiState.comment,
                onValueChange = { insulinViewModel.setComment(it)},
                enabled = uiState.canEdit,
                label = { Text(stringResource(R.string.comment)) })
    }

    if (showDatePicker.value) {
        AppDatePickerDialog(
            showState = showDatePicker,
            initialState = uiState.date,
            onDateSelected = {insulinViewModel.setDate(it)})
    }

    if (showTimePicker.value) {
        AppTimePickerDialog(
            showState = showTimePicker,
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onTimeSelected = { hour, minute ->
                insulinViewModel.setTime(hour, minute)
            }
        )
    }
}