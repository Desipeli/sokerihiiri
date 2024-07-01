package com.example.sokerihiiri.ui.screens.Measurement

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.SokerihiiriRepository
import java.time.LocalTime


class MeasurementViewModel(
    private val repository: SokerihiiriRepository
) : ViewModel() {
    var bloodSugarMeasurementState: BloodSugarMeasurementState by
        mutableStateOf(BloodSugarMeasurementState())
            private set

    fun setTime(hour: Int, minute: Int) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(hour = hour, minute = minute)
    }
    fun setDate(date: Long) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(date = date)
    }
    fun setTimeFromMeal(time: Int) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(timeFromMeal = time)
    }
    fun setBeforeMeal(beforeMeal: Boolean) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(beforeMeal = beforeMeal)
    }
    fun setValue(value: Float) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(value = value)
    }

    fun saveBloodSugarMeasurement() {
        try {
            val bloodSugarMeasurement = BloodSugarMeasurement(
                value = bloodSugarMeasurementState.value,
                timestamp = 0,
                beforeMeal = bloodSugarMeasurementState.beforeMeal,
                timeFromMeal = bloodSugarMeasurementState.timeFromMeal)


        } catch (e: Exception) {
            Log.d("AppViewModel", "saveBloodSugarMeasurement: ${e.message}")
        }

    }
}

data class BloodSugarMeasurementState(
    val value: Float = 0.0f,
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
    val date: Long = System.currentTimeMillis(),
    val beforeMeal: Boolean = false,
    val timeFromMeal: Int = 0,
)

class AppViewModelFactory(private val repository: SokerihiiriRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasurementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeasurementViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}