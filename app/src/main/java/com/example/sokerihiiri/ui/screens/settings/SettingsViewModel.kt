package com.example.sokerihiiri.ui.screens.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.SokerihiiriRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: SokerihiiriRepository
) : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            dataStoreManager.getDefaultInsulinDose().collect { dose ->
                uiState = uiState.copy(defaultInsulinDose = dose)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getDefaultHoursAfterMeal().collect { hours ->
                uiState = uiState.copy(defaultHoursAfterMeal = hours)
            }
        }

        viewModelScope.launch {
            dataStoreManager.getDefaultMinutesAfterMeal().collect { minutes ->
                uiState = uiState.copy(defaultMinutesAfterMeal = minutes)
            }
        }
    }

    fun setDefaultInsulinDose(dose: Int) {
        uiState = uiState.copy(defaultInsulinDose = dose)
    }

    fun setDefaultHoursAfterMeal(hours: Int) {
        uiState = uiState.copy(defaultHoursAfterMeal = hours)
    }

    fun setDefaultMinutesAfterMeal(minutes: Int) {
        uiState = uiState.copy(defaultMinutesAfterMeal = minutes)
    }

    private fun saveDefaultInsulinDose() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultInsulinDose(uiState.defaultInsulinDose)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default insulin dose", e)
            }
        }
    }

    private fun saveDefaultHoursAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultHoursAfterMeal(uiState.defaultHoursAfterMeal)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default hours after meal", e)
            }
        }
    }

    private fun saveDefaultMinutesAfterMeal() {
        viewModelScope.launch {
            try {
                dataStoreManager.setDefaultMinutesAfterMeal(uiState.defaultMinutesAfterMeal)
            } catch (e: Exception) {
                Log.e("SettingsViewModel", "Error saving default minutes after meal", e)
            }
        }
    }

    fun saveDefaultsSettings() {
        saveDefaultInsulinDose()
        saveDefaultHoursAfterMeal()
        saveDefaultMinutesAfterMeal()
    }

    fun deleteAllMeasurements() {
        viewModelScope.launch {
            repository.deleteAllBloodSugarMeasurements()
        }
    }

    fun deleteAllInsulinInjections() {
        viewModelScope.launch {
            repository.deleteAllInsulinInjections()
        }
    }

    fun deleteAllMeals() {
        viewModelScope.launch {
            repository.deleteAllMeals()
        }
    }

    fun deleteAllRoomData() {
        viewModelScope.launch {
            repository.deleteAllMeals()
        }
        viewModelScope.launch {
            repository.deleteAllBloodSugarMeasurements()
        }
        viewModelScope.launch {
            repository.deleteAllInsulinInjections()
        }
    }
}

data class UiState(
    val defaultInsulinDose: Int = 0,
    val defaultHoursAfterMeal: Int = 0,
    val defaultMinutesAfterMeal: Int = 0
)