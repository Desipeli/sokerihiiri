package com.example.sokerihiiri.ui.screens.measurement

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
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import kotlinx.coroutines.launch
import java.time.LocalTime


class MeasurementViewModel(
    private val repository: SokerihiiriRepository
) : ViewModel() {
    var uiState: BloodSugarMeasurementState by
        mutableStateOf(BloodSugarMeasurementState())
            private set

    fun setTime(hour: Int, minute: Int) {
        uiState = uiState.copy(hour = hour, minute = minute)
    }
    fun setDate(date: Long) {
        uiState = uiState.copy(date = date)
    }
    fun setHoursFromMeal(hours: Int) {
        var newHours = hours
        if (newHours < 0 ) newHours = 0

        val minutes = uiState.minutesFromMeal % 60
        uiState = uiState.copy(minutesFromMeal = newHours*60+minutes)
    }

    fun setMinutesFromMeal(minutes: Int) {
        var newMinutes = minutes
        if (newMinutes < 0 || minutes > 59) newMinutes = 0
        val hours = uiState.minutesFromMeal / 60 * 60
        Log.d("MeasurementViewModel", "Tunteja oli: $hours")
        uiState = uiState.copy(minutesFromMeal = hours+newMinutes)
    }
    fun setAfterMeal(afterMeal: Boolean) {
        uiState = uiState.copy(afterMeal = afterMeal)
    }
    fun setValue(value: Float) {
        uiState = uiState.copy(value = value)
    }

    private fun resetState() {
        uiState = BloodSugarMeasurementState()
    }

    fun saveBloodSugarMeasurement() {
        Log.d("MeasurementViewModel", "saveBloodSugarMeasurement state: $uiState")
        try {
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )

            Log.d("MeasurementViewModel", "saveBloodSugarMeasurement dateTime: $dateTime")

            val bloodSugarMeasurement = BloodSugarMeasurement(
                value = uiState.value,
                timestamp = dateTime,
                afterMeal = uiState.afterMeal,
                minutesFromMeal = uiState.minutesFromMeal)

            viewModelScope.launch {
                repository.insertBloodSugarMeasurement(bloodSugarMeasurement)
            }
            resetState()

        } catch (e: Exception) {
            Log.d("MeasurementViewModel", "saveBloodSugarMeasurement: ${e.message}")
        }
    }

    fun updateBloodSugarMeasurement() {
        Log.d("MeasurementViewModel", "updateBloodSugarMeasurement state: $uiState")
        try {
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val bloodSugarMeasurement = BloodSugarMeasurement(
                id = uiState.id!!,
                value = uiState.value,
                timestamp = dateTime,
                afterMeal = uiState.afterMeal,
                minutesFromMeal = uiState.minutesFromMeal)

            viewModelScope.launch {
                Log.d("MeasurementViewModel", "updateBloodSugarMeasurement: $bloodSugarMeasurement")
                repository.updateBloodSugarMeasurement(bloodSugarMeasurement)
            }
            resetState()
        } catch (e: Exception) {
            Log.d("MeasurementViewModel", "updateBloodSugarMeasurement: ${e.message}")
        }
    }

    fun getMeasurementFromId(id: String?) {
        if (id.toString() == uiState.id.toString()) return
        if (id == null) {
            resetState()
            return
        }
        viewModelScope.launch {
            val measurement = repository.getBloodSugarMeasurementById(id.toInt())
            val (hour, minute) = timestampToHoursAndMinutes(measurement.timestamp)
            uiState = BloodSugarMeasurementState(
                id = measurement.id,
                value = measurement.value,
                hour = hour,
                minute = minute,
                date = measurement.timestamp,
                afterMeal = measurement.afterMeal,
                minutesFromMeal = measurement.minutesFromMeal
            )
        }
    }

    fun deleteBloodSugarMeasurement(id: String) {
        try {
            viewModelScope.launch {
                repository.deleteBloodSugarMeasurementById(id.toInt())
            }
        } catch (e: Exception) {
            Log.d("MeasurementViewModel", "deleteBloodSugarMeasurement: ${e.message}")
        }
    }
}

data class BloodSugarMeasurementState(
    val id: Int? = null,
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