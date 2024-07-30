package com.example.sokerihiiri.ui.screens.others.other

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.sokerihiiri.ui.LocalOtherViewModel
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.StyledTextButton
import com.example.sokerihiiri.ui.components.StyledTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OtherScreen(
    id: String? = null,
) {
    // Näkymä muu-tapahtuman luomiseen ja muokkaukseen

    val otherViewModel = LocalOtherViewModel.current
    val uiState = otherViewModel.uiState

    LaunchedEffect(Unit) {
        try {
            if (id == null) {
                otherViewModel.resetState()
                otherViewModel.setCanEdit(true)
            } else {
                otherViewModel.getOtherById(id)
            }
        } catch (e: Exception) {
            Log.e("OtherScreen", "Error: ${e.message}")
        }
    }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    val showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            text = dateFormatter.format(
                Date(uiState.date)
            )
        )
        StyledTextField(
            value = uiState.comment,
            onValueChange = { otherViewModel.setComment(it)},
            enabled = uiState.canEdit,
            label = { Text(text = stringResource(R.string.comment)) })
    }

    if (showDatePicker.value) {
        AppDatePickerDialog(
            showState = showDatePicker,
            initialState = uiState.date,
            onDateSelected = {otherViewModel.setDate(it)})
    }

    if (showTimePicker.value) {
        AppTimePickerDialog(
            showState = showTimePicker,
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onTimeSelected = { hour, minute ->
                otherViewModel.setTime(hour, minute)
            }
        )
    }
}