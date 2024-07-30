package com.example.sokerihiiri.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import com.example.sokerihiiri.repository.Other
import com.example.sokerihiiri.repository.SokerihiiriRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel @Inject constructor(
    repository: SokerihiiriRepository
) : ViewModel() {
    /*
    ViewModel, joka tarjoaa tietokannasta tiedot kaikkiin selailunäkymiin.
     */
    val allMeasurements: LiveData<List<BloodSugarMeasurement>> =
        repository.allBloodSugarMeasurements.asLiveData()

    val allInjections: LiveData<List<InsulinInjection>> =
        repository.allInsulinInjections.asLiveData()

    val allMeals: LiveData<List<Meal>> =
        repository.allMeals.asLiveData()

    val allOthers: LiveData<List<Other>> =
        repository.allOthers.asLiveData()
}