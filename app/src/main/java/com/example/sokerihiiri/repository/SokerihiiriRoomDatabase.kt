package com.example.sokerihiiri.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//
//
// https://developer.android.com/codelabs/android-room-with-a-view-kotlin#7

@Database(
    entities =
    [
        BloodSugarMeasurement::class,
        InsulinInjection::class,
        Meal::class,
        Other::class
    ],
    version = 4,
    exportSchema = false
)
abstract class SokerihiiriDatabase : RoomDatabase() {
    abstract fun bloodSugarMeasurementDao(): BloodSugarMeasurementDao
    abstract fun insulinInjectionDao(): InsulinInjectionDao
    abstract fun mealDao(): MealDao
    abstract fun otherDao(): OtherDao
}

