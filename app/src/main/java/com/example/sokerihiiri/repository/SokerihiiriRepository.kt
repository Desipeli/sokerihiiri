package com.example.sokerihiiri.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SokerihiiriRepository(
    private val bloodSugarMeasurementDao: BloodSugarMeasurementDao,
    private val insulinInjectionDao: InsulinInjectionDao,
    private val mealDao: MealDao
) {

    val allBloodSugarMeasurements: Flow<List<BloodSugarMeasurement>> =
        bloodSugarMeasurementDao.getAll()

    val allInsulinInjections: Flow<List<InsulinInjection>> =
        insulinInjectionDao.getAll()

    val allMeals: Flow<List<Meal>> =
        mealDao.getAll()

    @WorkerThread
    suspend fun insertBloodSugarMeasurement(bloodSugarMeasurement: BloodSugarMeasurement) {
        bloodSugarMeasurementDao.insert(bloodSugarMeasurement)
    }

    @WorkerThread
    suspend fun insertInsulinInjection(insulinInjection: InsulinInjection) {
        insulinInjectionDao.insert(insulinInjection)
    }

    @WorkerThread
    suspend fun insertMeal(meal: Meal) {
        mealDao.insert(meal)
    }

}