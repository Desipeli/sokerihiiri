package com.example.sokerihiiri.ui.screens.Measurement

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import kotlinx.coroutines.launch
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
    fun setHoursFromMeal(hours: Int) {
        var newHours = hours
        if (newHours < 0 ) newHours = 0

        val minutes = bloodSugarMeasurementState.minutesFromMeal % 60
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(minutesFromMeal = newHours*60+minutes)
    }

    fun setMinutesFromMeal(minutes: Int) {
        var newMinutes = minutes
        if (newMinutes < 0 || minutes > 59) newMinutes = 0
        val hours = bloodSugarMeasurementState.minutesFromMeal / 60 * 60
        Log.d("AppViewModel", "Tunteja oli: $hours")
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(minutesFromMeal = hours+newMinutes)
    }
    fun setAfterMeal(afterMeal: Boolean) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(afterMeal = afterMeal)
    }
    fun setValue(value: Float) {
        bloodSugarMeasurementState = bloodSugarMeasurementState.copy(value = value)
    }

    fun saveBloodSugarMeasurement() {
        Log.d("AppViewModel", "saveBloodSugarMeasurement state: $bloodSugarMeasurementState")
        try {
            val dateTime = dateAndTimeToUTCLong(
                bloodSugarMeasurementState.date,
                bloodSugarMeasurementState.hour,
                bloodSugarMeasurementState.minute
            )

            Log.d("AppViewModel", "saveBloodSugarMeasurement dateTime: $dateTime")

            val bloodSugarMeasurement = BloodSugarMeasurement(
                value = bloodSugarMeasurementState.value,
                timestamp = dateTime,
                afterMeal = bloodSugarMeasurementState.afterMeal,
                minutesFromMeal = bloodSugarMeasurementState.minutesFromMeal)

            viewModelScope.launch {
                repository.insertBloodSugarMeasurement(bloodSugarMeasurement)
            }

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
    val afterMeal: Boolean = false,
    val minutesFromMeal: Int = 0,
)

class MeasurementViewModelFactory(private val repository: SokerihiiriRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeasurementViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeasurementViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}