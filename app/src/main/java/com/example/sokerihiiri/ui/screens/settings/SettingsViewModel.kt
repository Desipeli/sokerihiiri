package com.example.sokerihiiri.ui.screens.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokerihiiri.repository.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            dataStoreManager.getDefaultInsulinDose().collect {
                uiState = uiState.copy(defaultInsulinDose = it)
            }
        }
    }

    fun setDefaultInsulinDose(dose: Int) {
        uiState = uiState.copy(defaultInsulinDose = dose)
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

    fun saveSettings() {
        saveDefaultInsulinDose()
    }
}

data class UiState(
    val defaultInsulinDose: Int = 0
)