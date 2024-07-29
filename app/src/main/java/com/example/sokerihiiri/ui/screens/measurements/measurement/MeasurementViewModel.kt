package com.example.sokerihiiri.ui.screens.measurements.measurement

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.R
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.MAX_BLOOD_SUGAR_VALUE
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

class InvalidValueException(message: String) : Exception(message)

@HiltViewModel
class MeasurementViewModel @Inject constructor (
    private val repository: SokerihiiriRepository,
    private val dataStoreManager: DataStoreManager
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
        if (afterMeal) {
            viewModelScope.launch {
                dataStoreManager.getDefaultHoursAfterMeal().collect {
                    setHoursFromMeal(it)
                }
            }
            viewModelScope.launch {
                dataStoreManager.getDefaultMinutesAfterMeal().collect {
                    setMinutesFromMeal(it)
                }
            }
        } else {
            uiState = uiState.copy(minutesFromMeal = 0)
        }
    }
    fun setValue(value: Float) {
        if (value <= MAX_BLOOD_SUGAR_VALUE) {
            uiState = uiState.copy(value = value, valueError = null)
        }
    }

    fun setComment(comment: String) {
        uiState = uiState.copy(comment = comment)
    }

    fun resetState() {
        uiState = BloodSugarMeasurementState()
    }

    fun saveBloodSugarMeasurement(context: Context) {
        try {
            validateFields(context)
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
                minutesFromMeal = uiState.minutesFromMeal,
                comment = uiState.comment)

            viewModelScope.launch {
                repository.insertBloodSugarMeasurement(bloodSugarMeasurement)
            }
            resetState()

        } catch (e: InvalidValueException) {
            uiState = uiState.copy(valueError = e.message)
            throw e
        }
        catch (e: Exception) {
            Log.d("MeasurementViewModel", "saveBloodSugarMeasurement: ${e.message}")
        }
    }

    fun updateBloodSugarMeasurement(context: Context) {
        Log.d("MeasurementViewModel", "updateBloodSugarMeasurement state: $uiState")
        try {
            validateFields(context)
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
                minutesFromMeal = uiState.minutesFromMeal,
                comment = uiState.comment)

            viewModelScope.launch {
                Log.d("MeasurementViewModel", "updateBloodSugarMeasurement: $bloodSugarMeasurement")
                repository.updateBloodSugarMeasurement(bloodSugarMeasurement)
            }
            resetState()
        } catch (e: InvalidValueException) {
            uiState = uiState.copy(valueError = e.message)
            throw e
        }
        catch (e: Exception) {
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
                minutesFromMeal = measurement.minutesFromMeal,
                comment = measurement.comment,
            )
        }
    }

    fun deleteBloodSugarMeasurement() {
        try {
            viewModelScope.launch {
                repository.deleteBloodSugarMeasurementById(uiState.id!!)
            }
        } catch (e: Exception) {
            Log.d("MeasurementViewModel", "deleteBloodSugarMeasurement: ${e.message}")
        }
    }

    private fun validateFields(context: Context) {
        if (uiState.value <= 0.0f) {
            throw InvalidValueException(context.getString(R.string.blood_sugar_value_error))
        }
    }

    fun setCanEdit(canEdit: Boolean) {
        uiState = uiState.copy(canEdit = canEdit)
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
    val comment: String = "",
    val valueError: String? = null,
    val canEdit: Boolean = false
)