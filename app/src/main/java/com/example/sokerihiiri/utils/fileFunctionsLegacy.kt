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
                        writer.appendLine("Sep=${CSV_SEPARATOR}")
                        writer.appendLine("date${CSV_SEPARATOR}" +
                                "value${CSV_SEPARATOR}" +
                                "time_from_meal")
                        measurements.forEach { measurement ->
                            val (hoursFromMeal, minutesFromMeal) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                            writer.appendLine(
                                "${longToLocalDateTimeStringWithTimezone(measurement.timestamp)}${CSV_SEPARATOR}" +
                                        "${floatToCommaString(measurement.value)}${CSV_SEPARATOR}" +
                                        "${hoursFromMeal}:${minutesFromMeal}"
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
                        writer.appendLine("Sep=${CSV_SEPARATOR}")
                        writer.appendLine("date${CSV_SEPARATOR}" +
                                "dose")
                        insulinInjections.forEach { insulinInjection ->
                            writer.appendLine(
                                "${longToLocalDateTimeStringWithTimezone(insulinInjection.timestamp)}${CSV_SEPARATOR}" +
                                        "${insulinInjection.dose}"
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
                        writer.appendLine("Sep=${CSV_SEPARATOR}")
                        writer.appendLine("date${CSV_SEPARATOR}" +
                                "calories${CSV_SEPARATOR}" +
                                "carbohydrates${CSV_SEPARATOR}" +
                                "comment")
                        meals.forEach { meal ->
                            writer.appendLine(
                                "${longToLocalDateTimeStringWithTimezone(meal.timestamp)}${CSV_SEPARATOR}" +
                                        "${meal.calories}${CSV_SEPARATOR}" +
                                        "${meal.carbohydrates}${CSV_SEPARATOR}" +
                                        meal.comment
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
