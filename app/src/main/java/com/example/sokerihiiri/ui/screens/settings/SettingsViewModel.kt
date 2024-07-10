package com.example.sokerihiiri.ui.screens.settings

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.repository.SokerihiiriRepository
import com.example.sokerihiiri.utils.writeInsulinInjectionsToDownloadsCSVLegacy
import com.example.sokerihiiri.utils.writeMealsToDownloadsCSVLegacy
import com.example.sokerihiiri.utils.writeMeasurementsToDownloadsCSVLegacy
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

    fun writeCSVLegacy(context: Context) {
        try {
            viewModelScope.launch {
                try {
                    writeMeasurementsToDownloadsCSVLegacy(
                        measurementsFlow = repository.allBloodSugarMeasurements,
                    )
                } catch (e: Exception) {
                    Log.e("SettingsViewModel", "Error writing measurements to CSV legacy", e)
                }
            }
            viewModelScope.launch {
                try {
                    writeInsulinInjectionsToDownloadsCSVLegacy(
                        insulinInjectionsFlow = repository.allInsulinInjections
                    )
                } catch (e: Exception) {
                    Log.e("SettingsViewModel", "Error writing insulin injections to CSV legacy", e)
                }
            }
            viewModelScope.launch {
                try {
                    writeMealsToDownloadsCSVLegacy(
                        mealsFlow = repository.allMeals
                    )
                } catch (e: Exception) {
                    Log.e("SettingsViewModel", "Error writing meals to CSV legacy", e)
                }
            }
            Toast.makeText(context, "Tiedot tallennettu ${Environment.DIRECTORY_DOWNLOADS} hakemistoon", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("SettingsViewModel", "Error writing CSV legacy", e)
            Toast.makeText(context, "Tietojen tallennus ei onnistunut", Toast.LENGTH_SHORT).show()
        }
    }

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