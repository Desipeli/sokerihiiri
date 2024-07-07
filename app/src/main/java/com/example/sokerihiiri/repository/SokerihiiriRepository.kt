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
    suspend fun getBloodSugarMeasurementById(id: Int): BloodSugarMeasurement {
        return bloodSugarMeasurementDao.getById(id)
    }

    @WorkerThread
    suspend fun updateBloodSugarMeasurement(bloodSugarMeasurement: BloodSugarMeasurement) {
        bloodSugarMeasurementDao.update(bloodSugarMeasurement)
    }

    @WorkerThread
    suspend fun deleteBloodSugarMeasurementById(id: Int) {
        bloodSugarMeasurementDao.deleteById(id)
    }

    @WorkerThread
    suspend fun insertInsulinInjection(insulinInjection: InsulinInjection) {
        insulinInjectionDao.insert(insulinInjection)
    }

    @WorkerThread
    suspend fun updateInsulinInjection(insulinInjection: InsulinInjection) {
        insulinInjectionDao.update(insulinInjection)
    }

    @WorkerThread
    suspend fun getInsulinInjectionById(id: Int): InsulinInjection {
        return insulinInjectionDao.getById(id)
    }

    @WorkerThread
    suspend fun deleteInsulinInjectionById(toInt: Int) {
        insulinInjectionDao.deleteById(toInt)
    }

    @WorkerThread
    suspend fun insertMeal(meal: Meal) {
        mealDao.insert(meal)
    }

    @WorkerThread
    suspend fun getMealById(id: String): Meal {
        return mealDao.getById(id)
    }

    @WorkerThread
    suspend fun updateMeal(meal: Meal) {
        mealDao.update(meal)
    }

    @WorkerThread
    suspend fun deleteMealById(id: Int) {
        mealDao.deleteById(id)
    }
}