package com.example.sokerihiiri.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

//
//
// https://developer.android.com/codelabs/android-room-with-a-view-kotlin#5

// Kaikki mahdolliset tietokantakyselyt
@Dao
interface BloodSugarMeasurementDao {
    @Query("SELECT * FROM blood_sugar_measurements ORDER BY timestamp DESC")
    fun getAll(): Flow<List<BloodSugarMeasurement>>

    @Query("SELECT * FROM blood_sugar_measurements")
    suspend fun getAllAsList(): List<BloodSugarMeasurement>

    @Query("SELECT * FROM blood_sugar_measurements WHERE id = :id")
    suspend fun getById(id: Int): BloodSugarMeasurement

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(measurement: BloodSugarMeasurement)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(measurements: List<BloodSugarMeasurement>)

    @Update
    suspend fun update(measurement: BloodSugarMeasurement)

    @Query("DELETE FROM blood_sugar_measurements WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM blood_sugar_measurements")
    suspend fun deleteAll()
}

@Dao
interface InsulinInjectionDao {
    @Query("SELECT * FROM insulin_injections ORDER BY timestamp DESC")
    fun getAll(): Flow<List<InsulinInjection>>

    @Query("SELECT * FROM insulin_injections")
    suspend fun getAllAsList(): List<InsulinInjection>

    @Query("SELECT * FROM insulin_injections WHERE id = :id")
    suspend fun getById(id: Int): InsulinInjection

    @Query("SELECT * FROM insulin_injections WHERE timestamp>= :start AND timestamp<= :end")
    suspend fun getInjectionsByTimestampRange(start: Long, end: Long): List<InsulinInjection>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(injection: InsulinInjection)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(injections: List<InsulinInjection>)

    @Update
    suspend fun update(injection: InsulinInjection)

    @Query("DELETE FROM insulin_injections WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM insulin_injections")
    suspend fun deleteAll()
}

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Meal>>

    @Query("SELECT * FROM meals")
    suspend fun getAllAsList(): List<Meal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(meals: List<Meal>)

    @Query("DELETE FROM meals")
    suspend fun deleteAll()
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getById(id: String): Meal
    @Update
    suspend fun update(meal: Meal)

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface OtherDao {
    @Query("SELECT * FROM others ORDER BY timestamp DESC")
    fun getAll(): Flow<List<Other>>

    @Query("SELECT * FROM others")
    suspend fun getAllAsList(): List<Other>

    @Query("SELECT * FROM others WHERE id = :id")
    suspend fun getById(id: Int): Other

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(other: Other)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMany(other: List<Other>)

    @Update
    suspend fun update(other: Other)

    @Query("DELETE FROM others WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM others")
    suspend fun deleteAll()
}