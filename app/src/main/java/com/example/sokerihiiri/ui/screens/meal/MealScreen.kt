package com.example.sokerihiiri.ui.screens.meal

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokerihiiri.ui.components.AppDatePickerDialog
import com.example.sokerihiiri.ui.components.AppTimePickerDialog
import com.example.sokerihiiri.ui.components.NumberTextField
import com.example.sokerihiiri.ui.components.StyledTextField
import com.example.sokerihiiri.ui.components.ViewAndEdit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MealScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    id: String? = null
) {
    val mealViewModel: MealViewModel = hiltViewModel()
    var allowEdit: MutableState<Boolean> = remember { mutableStateOf(false) }

    LaunchedEffect(id) {
        try {
            if (id == null) allowEdit.value = true
            mealViewModel.getMealById(id)
        } catch (e: Exception) {
            Log.e("MealScreen", "Error getting meal", e)
        }
    }

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

    ViewAndEdit(
        navController = navController,
        id = id,
        allowEdit = allowEdit,
        save = { mealViewModel.saveMeal() },
        update = { mealViewModel.updateMeal() },
        delete = { mealViewModel.deleteMealById() }) {

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
                label = { Text(text = "Kcal")},
                enabled = allowEdit.value,
                isError = uiState.caloriesError != null
            )
            Text(text = uiState.caloriesError ?: "")
            NumberTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                value = if (uiState.carbohydrates == 0) "" else uiState.carbohydrates.toString(),
                onValueChange = { handleChangeCarbs(it) },
                label = { Text(text = "Hiilihydraatit")},
                enabled = allowEdit.value,
                isError = uiState.carbohydratesError != null
            )
            Text(text = uiState.carbohydratesError ?: "")

            Spacer(modifier = Modifier.height(64.dp))
            TextButton(onClick = { showTimePicker.value = true }, enabled = allowEdit.value) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        uiState.hour,
                        uiState.minute
                    )
                )
            }
            TextButton(onClick = { showDatePicker.value = true }, enabled = allowEdit.value) {
                Text(dateFormatter.format(Date(uiState.date)))
            }
            StyledTextField(
                value = uiState.comment,
                onValueChange = {handleChangeComment(it)},
                label = {Text("Kommentti")},
                enabled = allowEdit.value)
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