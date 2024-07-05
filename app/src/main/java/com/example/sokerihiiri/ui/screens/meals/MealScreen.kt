package com.example.sokerihiiri.ui.screens.meals

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.StyledTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MealScreen(
    mealViewModel: MealViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val uiState = mealViewModel.uiState

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var showDatePicker: MutableState<Boolean> = remember { mutableStateOf(false) }
    var showTimePicker: MutableState<Boolean> = remember { mutableStateOf(false) }

    fun handleCalorieChange(value: String) {
        try {
            if (value.isEmpty()) {
                mealViewModel.setCalories(0)
            } else {
                mealViewModel.setCalories(value.toInt())
            }
        } catch (e: Exception) {
            Log.e("MealScreen", "Error setting calories", e)
        }
    }

    fun handleChangeCarbs(value: String) {
        try {
            if (value.isEmpty()) {
                mealViewModel.setCarbs(0)
            } else {
                mealViewModel.setCarbs(value.toInt())
            }
        } catch (e: Exception) {
            Log.e("MealScreen", "Error setting carbs", e)
        }
    }

    fun handleChangeComment(value: String) {
        try {
            mealViewModel.setComment(value)
        } catch (e: Exception) {
            Log.e("MealScreen", "Error setting comment", e)
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
            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = if (uiState.calories == 0) "" else uiState.calories.toString(),
                onValueChange = { handleCalorieChange(it) },
                label = { Text(text = "Kcal")}
            )
            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = if (uiState.carbs == 0) "" else uiState.carbs.toString(),
                onValueChange = { handleChangeCarbs(it) },
                label = { Text(text = "Hiilihydraatit")}
            )
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
            StyledTextField(
                value = uiState.comment,
                onValueChange = {handleChangeComment(it)},
                label = {Text("Kommentti")})
        }
        TextButton(modifier = Modifier
            .align(Alignment.BottomEnd),
            onClick = {
                mealViewModel.saveMeal()
                navController.navigateUp()
            }) {
            Text("Tallenna")
        }
    }

    if (showDatePicker.value) {
        AppDatePickerDialog(
            showState = showDatePicker,
            initialState = uiState.date,
            onDateSelected = { mealViewModel.setDate(it) } )
    }

    if (showTimePicker.value) {
        AppTimePickerDialog(
            showState = showTimePicker,
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onTimeSelected = { hour, minute ->
                mealViewModel.setTime(hour, minute)
            }
        )
    }
}