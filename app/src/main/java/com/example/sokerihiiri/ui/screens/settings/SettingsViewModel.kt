package com.example.sokerihiiri.ui.screens.settings

import android.content.Context
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
class SettingsViewModel @Inject constructor() : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState())
        private set

    fun getDefaultInsulinDose(context: Context) {
        val defaultInsulinDose = DataStoreManager.getDefaultInsulinDose(context)
//        uiState = uiState.copy(
//            defaultInsulinDose = defaultInsulinDose
//        )
    }

    fun setDefaultInsulinDose(context: Context, value: Int) {
        viewModelScope.launch {
            DataStoreManager.setDefaultInsulinDose(context, value)
        }
    }
}

data class UiState (
    val defaultInsulinDose: Int = 0
)