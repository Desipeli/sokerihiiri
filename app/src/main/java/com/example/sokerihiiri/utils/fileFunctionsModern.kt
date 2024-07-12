package com.example.sokerihiiri.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun writeMeasurementsToDownloadsCSVModern(
    context: Context,
    measurementsFlow: Flow<List<BloodSugarMeasurement>>
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "measurements.csv")
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let { fileUri ->
        try {
            withContext(Dispatchers.IO) {
                measurementsFlow.collect { measurements ->
                    resolver.openOutputStream(fileUri)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.appendLine("Sep=${CSV_SEPARATOR}")
                            writer.appendLine("date${CSV_SEPARATOR}" +
                                    "value${CSV_SEPARATOR}" +
                                    "time_from_meal")
                            measurements.forEach { measurement ->
                                val (hoursFromMeal, minutesFromMeal) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                                writer.appendLine("${longToLocalDateTimeStringWithTimezone(measurement.timestamp)}${CSV_SEPARATOR}" +
                                        "${measurement.value}${CSV_SEPARATOR}" +
                                        "${hoursFromMeal}:${minutesFromMeal}")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("writeCSVToDownloads", "Error writing CSV to Downloads", e)
            throw e
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun writeInsulinInjectionsToDownloadsCSVModern(
    context: Context,
    insulinInjectionsFlow: Flow<List<InsulinInjection>>
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "insulin_injections.csv")
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let { fileUri ->
        try {
            withContext(Dispatchers.IO) {
                insulinInjectionsFlow.collect { insulinInjections ->
                    resolver.openOutputStream(fileUri)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.appendLine("Sep=${CSV_SEPARATOR}")
                            writer.appendLine("date${CSV_SEPARATOR}" +
                                    "dose")
                            insulinInjections.forEach { insulinInjection ->
                                writer.appendLine("${longToLocalDateTimeStringWithTimezone(insulinInjection.timestamp)}${CSV_SEPARATOR}" +
                                        "${insulinInjection.dose}")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("writeCSVToDownloads", "Error writing CSV to Downloads", e)
            throw e
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun writeMealsToDownloadsCSVModern(
    context: Context,
    mealsFlow: Flow<List<Meal>>
) {
    val contentValues = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "meals.csv")
        put(MediaStore.Downloads.MIME_TYPE, "text/csv")
        put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }

    val resolver = context.contentResolver
    val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    uri?.let { fileUri ->
        try {
            withContext(Dispatchers.IO) {
                mealsFlow.collect { meals ->
                    resolver.openOutputStream(fileUri)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.appendLine("Sep=${CSV_SEPARATOR}")
                            writer.appendLine("date${CSV_SEPARATOR}" +
                                    "calories${CSV_SEPARATOR}" +
                                    "carbohydrates${CSV_SEPARATOR}" +
                                    "comment")
                            meals.forEach { meal ->
                                writer.appendLine("${longToLocalDateTimeStringWithTimezone(meal.timestamp)}${CSV_SEPARATOR}" +
                                        "${meal.calories}${CSV_SEPARATOR}" +
                                        "${meal.carbohydrates}${CSV_SEPARATOR}" +
                                        meal.comment)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("writeCSVToDownloads", "Error writing CSV to Downloads", e)
            throw e
        }
    }
}