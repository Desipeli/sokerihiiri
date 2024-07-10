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
                            writer.appendLine("date,value,minutes_from_meal") // Header
                            measurements.forEach { measurement ->
                                writer.appendLine("${longToUtcTimestamp(measurement.timestamp)},${measurement.value},${measurement.minutesFromMeal}")
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
                            writer.appendLine("date,dose") // Header
                            insulinInjections.forEach { insulinInjection ->
                                writer.appendLine("${longToUtcTimestamp(insulinInjection.timestamp)},${insulinInjection.dose}")
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
                            writer.appendLine("date, calories, carbohydrates, comment") // Header
                            meals.forEach { meal ->
                                writer.appendLine("${longToUtcTimestamp(meal.timestamp)},${meal.calories},${meal.carbohydrates},${meal.comment}")
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