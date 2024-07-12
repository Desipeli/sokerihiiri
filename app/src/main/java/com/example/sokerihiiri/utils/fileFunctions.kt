package com.example.sokerihiiri.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import kotlinx.coroutines.flow.Flow
import java.io.OutputStreamWriter
suspend fun writeMeasurementsToDownloadsCSV(
    context: Context,
    measurements: List<BloodSugarMeasurement>?,
    fileUri: Uri,
) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine("date${CSV_SEPARATOR}" +
                        "value${CSV_SEPARATOR}" +
                        "time_from_meal")
                writer.flush()
                measurements?.forEach { measurement ->
                    val (hoursFromMeal, minutesFromMeal) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(measurement.timestamp)}${CSV_SEPARATOR}" +
                        "${floatToCommaString(measurement.value)}${CSV_SEPARATOR}" +
                        "${hoursFromMeal}:${minutesFromMeal}"
                    )
                    writer.flush()
                    }
                }
            }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}

suspend fun writeInsulinInjectionsToDownloadsCSV(
    context: Context,
    insulinInjections: List<InsulinInjection>?,
    fileUri: Uri,
) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine(
                    "date${CSV_SEPARATOR}" +
                            "dose"
                )
                writer.flush()
                insulinInjections?.forEach { insulinInjection ->
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(insulinInjection.timestamp)}${CSV_SEPARATOR}" +
                                "${insulinInjection.dose}"
                    )
                    writer.flush()
                }
            }
        }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}

suspend fun writeMealsToDownloadsCSV(
    context: Context,
    meals: List<Meal>?,
    fileUri: Uri,
) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine(
                    "date${CSV_SEPARATOR}" +
                            "calories${CSV_SEPARATOR}" +
                            "carbohydrates${CSV_SEPARATOR}" +
                            "comment"
                )
                writer.flush()
                meals?.forEach { meal ->
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(meal.timestamp)}${CSV_SEPARATOR}" +
                                "${meal.calories}${CSV_SEPARATOR}" +
                                "${meal.carbohydrates}${CSV_SEPARATOR}" +
                                meal.comment
                    )
                    writer.flush()
                }
            }
        }
    } catch (e: Exception) {
        Log.e("writeCSVToDownloads", "Error writing CSV file", e)
        throw e
    }
}
