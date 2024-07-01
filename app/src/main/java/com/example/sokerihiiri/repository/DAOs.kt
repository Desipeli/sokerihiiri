package com.example.sokerihiiri.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//
//
// https://developer.android.com/codelabs/android-room-with-a-view-kotlin#5

@Dao
interface BloodSugarMeasurementDao {
    @Query("SELECT * FROM blood_sugar_measurements")
    fun getAll(): Flow<List<BloodSugarMeasurement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(measurement: BloodSugarMeasurement)

    @Query("DELETE FROM blood_sugar_measurements")
    suspend fun deleteAll()
}

@Dao
interface InsulinInjectionDao {
    @Query("SELECT * FROM insulin_injections")
    fun getAll(): Flow<List<InsulinInjection>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(injection: InsulinInjection)

    @Query("DELETE FROM insulin_injections")
    suspend fun deleteAll()
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
    fun getAll(): Flow<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Query("DELETE FROM meals")
    suspend fun deleteAll()

}