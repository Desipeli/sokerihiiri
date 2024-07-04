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
import kotlinx.coroutines.launch
import java.time.LocalTime

class InsulinViewModel(
    private val repository: SokerihiiriRepository
): ViewModel() {

    var uiState: InsulinUiState by mutableStateOf(InsulinUiState())
        private set

    fun setDose(dose: Int) {
        uiState = uiState.copy(dose = dose)
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
        } catch (e: Exception) {
            Log.e("InsulinViewModel", "Error saving insulin injection", e)
        }
    }
}

data class InsulinUiState(
    val dose: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val hour: Int = LocalTime.now().hour,
    val minute: Int = LocalTime.now().minute,
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