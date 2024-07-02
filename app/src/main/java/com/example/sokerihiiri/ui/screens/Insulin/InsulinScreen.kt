package com.example.sokerihiiri.ui.screens.Insulin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsulinScreen(
    insulinViewModel: InsulinViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = insulinViewModel.uiState

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    var showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    fun handleDoseChange(newDose: String) {
        try {
            insulinViewModel.setDose(newDose.toFloat())
        } catch (e: Exception) {
            Log.e("InsulinScreen", "Failed to parse dose", e)
        }
    }

    Box(modifier = Modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NumberTextField(
                modifier = Modifier.fillMaxWidth(0.5f),
                value = if (uiState.dose <= 0.0f) "" else uiState.dose.toString(),
                onValueChange = {handleDoseChange(it)},
                label = { Text("Insuliiniannos") })
            Spacer(modifier = Modifier.height(64.dp))
            TextButton(onClick = { showTimePicker.value = true }) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        uiState.hour,
                        uiState.minute
                    )
                )
            }
            TextButton(onClick = { showDatePicker.value = true }) {
                Text(dateFormatter.format(Date(uiState.date)))
            }
        }
        TextButton(modifier = Modifier
            .align(Alignment.BottomEnd),
            onClick = {
                insulinViewModel.saveInsulinInjection()
            }) {
            Text("Tallenna")
        }
    }
    if (showDatePicker.value) {
        AppDatePickerDialog(
            showState = showDatePicker,
            initialState = uiState.date,
            onDateSelected = {insulinViewModel.setDate(it)} )
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