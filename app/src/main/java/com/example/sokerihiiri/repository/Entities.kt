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
    @ColumnInfo(name = "after_meal") val afterMeal: Boolean,
    @ColumnInfo(name = "minutes_from_meal") val minutesFromMeal: Int,
    @ColumnInfo(name = "comment") val comment: String
)

@Entity(tableName = "insulin_injections")
class InsulinInjection(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "dose") val dose: Int,
    @ColumnInfo(name = "comment") val comment: String
)

@Entity(tableName = "meals")
class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "calories") val calories: Int,
    @ColumnInfo(name = "carbohydrates") val carbohydrates: Int,
    @ColumnInfo(name = "comment") val comment: String,
)

@Entity(tableName = "others")
class Other(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "comment") val comment: String,
)