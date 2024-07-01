package com.example.sokerihiiri

import androidx.lifecycle.ViewModel
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.SokerihiiriRepository

class AppViewModel(
    private val repository: SokerihiiriRepository
) : ViewModel() {
    
}