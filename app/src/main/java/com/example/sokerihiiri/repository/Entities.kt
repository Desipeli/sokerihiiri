package com.example.sokerihiiri.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "blood_sugar_measurements")
class BloodSugarMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "value") val value: Float,
    @ColumnInfo(name = "before_meal") val beforeMeal: Boolean,
    @ColumnInfo(name = "time_from_meal") val timeFromMeal: Int
)

@Entity(tableName = "insulin_injections")
class InsulinInjection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "dose") val dose: Double
)

@Entity(tableName = "meals")
class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "calories") val calories: String,
    @ColumnInfo(name = "carbohydrates") val carbohydrates: String,
)