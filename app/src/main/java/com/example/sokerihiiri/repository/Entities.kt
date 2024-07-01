package com.example.sokerihiiri.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "blood_sugar_measurements")
class BloodSugarMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Timestamp,
    @ColumnInfo(name = "value") val value: Double
)

@Entity(tableName = "insulin_injections")
class InsulinInjection(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Timestamp,
    @ColumnInfo(name = "dose") val dose: Double
)

@Entity(tableName = "meals")
class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "timestamp") val timestamp: Timestamp,
    @ColumnInfo(name = "calories") val calories: String,
    @ColumnInfo(name = "carbohydrates") val carbohydrates: String,
)