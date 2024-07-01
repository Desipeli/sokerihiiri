package com.example.sokerihiiri.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sokerihiiri.repository.SokerihiiriRepository


class AppViewModel(
    private val repository: SokerihiiriRepository
) : ViewModel() {
    var bloodSugarMeasurementState: BloodSugarMeasurementState by
        mutableStateOf(BloodSugarMeasurementState())
            private set


}

data class BloodSugarMeasurementState(
    val value: Float? = null,
    val time: String = "",
    val beforeMeal: Boolean = false,
    val timeFromMeal: String = "",
)

class AppViewModelFactory(private val repository: SokerihiiriRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}