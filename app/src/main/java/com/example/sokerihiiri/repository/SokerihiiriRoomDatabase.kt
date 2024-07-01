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
        Meal::class
    ],
    version = 1,
    exportSchema = false
)
public abstract class SokerihiiriDatabase : RoomDatabase() {
    abstract fun bloodSugarMeasurementDao(): BloodSugarMeasurementDao
    abstract fun insulinInjectionDao(): InsulinInjectionDao
    abstract fun mealDao(): MealDao

    companion object {
        // singleton
        @Volatile
        private var INSTANCE: SokerihiiriDatabase? = null

        fun getDatabase(context: Context): SokerihiiriDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SokerihiiriDatabase::class.java,
                    "sokerihiiri_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

