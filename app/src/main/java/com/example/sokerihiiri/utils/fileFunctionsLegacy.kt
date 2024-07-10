package com.example.sokerihiiri.utils

import android.os.Environment
import android.util.Log
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

suspend fun writeMeasurementsToDownloadsCSVLegacy(
    measurementsFlow: Flow<List<BloodSugarMeasurement>>? = null,
) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val measurementsFile = File(downloadsDir, "measurements.csv")
    try {
        withContext(Dispatchers.IO) {
            measurementsFlow?.collect { measurements ->
                Log.d("writeCSVToDownloads", "measurements: $measurements")
                withContext(Dispatchers.IO) {
                    FileWriter(measurementsFile).use { writer ->
                        writer.appendLine("date,value,minutes_from_meal")
                        measurements.forEach { measurement ->
                            writer.appendLine(
                                "${longToUtcTimestamp(measurement.timestamp)},${measurement.value},${measurement.minutesFromMeal}"
                            )
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}
suspend fun writeInsulinInjectionsToDownloadsCSVLegacy(
    insulinInjectionsFlow: Flow<List<InsulinInjection>>? = null,
) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val insulinInjectionsFile = File(downloadsDir, "insulin_injections.csv")
    try {
        withContext(Dispatchers.IO) {
            insulinInjectionsFlow?.collect { insulinInjections ->
                Log.d("writeCSVToDownloads", "insulin: $insulinInjectionsFlow")
                withContext(Dispatchers.IO) {
                    FileWriter(insulinInjectionsFile).use { writer ->
                        writer.appendLine("date,dose")
                        insulinInjections.forEach { insulinInjection ->
                            writer.appendLine(
                                "${longToUtcTimestamp(insulinInjection.timestamp)},${insulinInjection.dose}"
                            )
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}

suspend fun writeMealsToDownloadsCSVLegacy(
    mealsFlow: Flow<List<Meal>>? = null,
) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val mealsFile = File(downloadsDir, "meals.csv")
    try {
        withContext(Dispatchers.IO) {
            mealsFlow?.collect { meals ->
                Log.d("writeCSVToDownloads", "meals: $mealsFlow")
                withContext(Dispatchers.IO) {
                    FileWriter(mealsFile).use { writer ->
                        writer.appendLine("date,calories,carbohydrates,comment")
                        meals.forEach { meal ->
                            writer.appendLine(
                                "${longToUtcTimestamp(meal.timestamp)},${meal.calories},${meal.carbohydrates},${meal.comment}"
                            )
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}

