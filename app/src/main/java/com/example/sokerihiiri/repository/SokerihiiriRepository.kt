package com.example.sokerihiiri.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SokerihiiriRepository @Inject constructor(
    private val bloodSugarMeasurementDao: BloodSugarMeasurementDao,
    private val insulinInjectionDao: InsulinInjectionDao,
    private val mealDao: MealDao,
    private val otherDao: OtherDao,
) {
    // Repositorioluokka. Yhteys tietokannan ja ViewModelien välille.

    // Kaikkien taulujen seuranta Flown avulla
    val allBloodSugarMeasurements: Flow<List<BloodSugarMeasurement>> =
        bloodSugarMeasurementDao.getAll()

    val allInsulinInjections: Flow<List<InsulinInjection>> =
        insulinInjectionDao.getAll()

    val allMeals: Flow<List<Meal>> =
        mealDao.getAll()

    val allOthers: Flow<List<Other>> =
        otherDao.getAll()

    // Tietokantaoperaatiot, suoritetaan erillisissä säikeissä.
    @WorkerThread
    suspend fun getAllBloodSugarMeasurementsAsList(): List<BloodSugarMeasurement> {
        return bloodSugarMeasurementDao.getAllAsList()
    }

    @WorkerThread
    suspend fun insertBloodSugarMeasurement(bloodSugarMeasurement: BloodSugarMeasurement) {
        bloodSugarMeasurementDao.insert(bloodSugarMeasurement)
    }

    @WorkerThread
    suspend fun insertManyBloodSugarMeasurements(bloodSugarMeasurements: List<BloodSugarMeasurement>) {
        bloodSugarMeasurementDao.insertMany(bloodSugarMeasurements)
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
    suspend fun deleteAllBloodSugarMeasurements() {
        bloodSugarMeasurementDao.deleteAll()
    }

    @WorkerThread
    suspend fun getAllInsulinInjectionsAsList(): List<InsulinInjection> {
        return insulinInjectionDao.getAllAsList()
    }

    @WorkerThread
    suspend fun getInjectionsByTimestampRange(start: Long, end: Long): List<InsulinInjection> {
        return insulinInjectionDao.getInjectionsByTimestampRange(start, end)
    }

    @WorkerThread
    suspend fun insertInsulinInjection(insulinInjection: InsulinInjection) {
        insulinInjectionDao.insert(insulinInjection)
    }

    @WorkerThread
    suspend fun insertManyInsulinInjections(insulinInjections: List<InsulinInjection>) {
        insulinInjectionDao.insertMany(insulinInjections)
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
    suspend fun deleteAllInsulinInjections() {
        insulinInjectionDao.deleteAll()
    }

    @WorkerThread
    suspend fun getAllMealsAsList(): List<Meal> {
        return mealDao.getAllAsList()
    }

    @WorkerThread
    suspend fun insertMeal(meal: Meal) {
        mealDao.insert(meal)
    }

    @WorkerThread
    suspend fun insertManyMeals(meals: List<Meal>) {
        mealDao.insertMany(meals)
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

    @WorkerThread
    suspend fun deleteAllMeals() {
        mealDao.deleteAll()
    }

    @WorkerThread
    suspend fun getOtherById(id: Int): Other {
        return otherDao.getById(id)
    }

    @WorkerThread
    suspend fun insertOther(other: Other) {
        otherDao.insert(other)
    }

    @WorkerThread
    suspend fun insertManyOthers(others: List<Other>) {
        otherDao.insertMany(others)
    }

    @WorkerThread
    suspend fun updateOther(other: Other) {
        otherDao.update(other)
    }

    @WorkerThread
    suspend fun deleteOtherById(id: Int) {
        otherDao.deleteById(id)
    }

    @WorkerThread
    suspend fun deleteAllOthers() {
        otherDao.deleteAll()
    }

    @WorkerThread
    suspend fun getAllOthersAsList(): List<Other> {
        return otherDao.getAllAsList()
    }


}