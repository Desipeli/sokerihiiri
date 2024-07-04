package com.example.sokerihiiri.ui.screens.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.SokerihiiriRepository

class BrowseViewModel(
    private val repository: SokerihiiriRepository
) : ViewModel() {

    val allMeasurements: LiveData<List<BloodSugarMeasurement>> =
        repository.allBloodSugarMeasurements.asLiveData()

    val allInjections: LiveData<List<InsulinInjection>> =
        repository.allInsulinInjections.asLiveData()

}

class BrowseViewModelFactory(private val repository: SokerihiiriRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrowseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BrowseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}