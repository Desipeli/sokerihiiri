package com.example.sokerihiiri.ui.screens.browse

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import com.example.sokerihiiri.repository.SokerihiiriRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    private val repository: SokerihiiriRepository
) : ViewModel() {

    val allMeasurements: LiveData<List<BloodSugarMeasurement>> =
        repository.allBloodSugarMeasurements.asLiveData()

    val allInjections: LiveData<List<InsulinInjection>> =
        repository.allInsulinInjections.asLiveData()

    val allMeals: LiveData<List<Meal>> =
        repository.allMeals.asLiveData()

}