package com.example.sokerihiiri.ui.screens.insulin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.dateAndTimeToUTCLong
import com.example.sokerihiiri.utils.timestampToHoursAndMinutes
import kotlinx.coroutines.launch
import java.time.LocalTime

class InvalidDoseException(message: String) : Exception(message)

class InsulinViewModel(
    private val repository: SokerihiiriRepository
): ViewModel() {

    var uiState: InsulinUiState by mutableStateOf(InsulinUiState())
        private set

    fun setDose(dose: Int) {
        uiState = uiState.copy(dose = dose, doseError = null)
    }

    fun setDate(date: Long) {
        uiState = uiState.copy(date = date)
    }

    fun setTime(hour: Int, minute: Int) {
        uiState = uiState.copy(hour = hour, minute = minute)
    }

    private fun resetState() {
        uiState = InsulinUiState()
    }

    fun saveInsulinInjection() {
        try {
            validateFields()
            Log.d("saveInsulinInjection", "Saving insulin injection")
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val insulinInjection = InsulinInjection(
                dose = uiState.dose,
                timestamp = dateTime
            )

            viewModelScope.launch {
                repository.insertInsulinInjection(insulinInjection)
            }
            resetState()
        } catch (e: InvalidDoseException) {
            uiState = uiState.copy(doseError = e.message)
            throw e
        }
        catch (e: Exception) {
            Log.e("InsulinViewModel", "Error saving insulin injection", e)
        }
    }

    fun getInsulinInjectionById(id: String?) {
        if (id.toString() == uiState.id.toString()) return
        if (id == null) {
            resetState()
            return
        }

        viewModelScope.launch {
            val insulinInjection = repository.getInsulinInjectionById(id.toInt())
            val (hour, minute) = timestampToHoursAndMinutes(insulinInjection.timestamp)
            uiState = InsulinUiState(
                id = insulinInjection.id,
                dose = insulinInjection.dose,
                date = insulinInjection.timestamp,
                hour = hour,
                minute = minute
            )
        }
    }

    fun updateInsulinInjection() {
        try {
            validateFields()
            val dateTime = dateAndTimeToUTCLong(
                uiState.date,
                uiState.hour,
                uiState.minute
            )
            val insulinInjection = InsulinInjection(
                id = uiState.id!!,
                dose = uiState.dose,
                timestamp = dateTime
            )
            viewModelScope.launch {
                repository.updateInsulinInjection(insulinInjection)
            }
        } catch (e: InvalidDoseException) {
            uiState = uiState.copy(doseError = e.message)
            Log.e("InsulinViewModel", "Invalid dose", e)
            throw e
        } catch (e: Exception) {
            Log.e("InsulinViewModel", "Error updating insulin injection", e)
        }
    }

    fun deleteInsulinInjectionById() {
        try {
            viewModelScope.launch {
                repository.deleteInsulinInjectionById(uiState.id!!)
            }
        } catch (e: Exception) {
            Log.e("InsulinViewModel", "Error deleting insulin injection", e)
        }
    }

    private fun validateFields() {
        Log.d("validateFields", "Validating fields")
        Log.d("validateFields", "dose: ${uiState.dose}")
        if (uiState.dose <= 0) {
            throw InvalidDoseException("Annoksen oltava suurempi kuin 0")
        }
    }
}

data class InsulinUiState(
    val id: Int? = null,
    val dose: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
    val doseError: String? = null
    )

class InsulinViewModelFactory(private val repository: SokerihiiriRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InsulinViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InsulinViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}