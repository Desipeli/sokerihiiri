package com.example.sokerihiiri.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.sokerihiiri.repository.BloodSugarMeasurement
import com.example.sokerihiiri.repository.InsulinInjection
import com.example.sokerihiiri.repository.Meal
import com.example.sokerihiiri.repository.Other
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
suspend fun writeMeasurementsToDownloadsCSV(
    context: Context,
    measurements: List<BloodSugarMeasurement>?,
    fileUri: Uri,
) = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine("#measurements")
                writer.appendLine("date${CSV_SEPARATOR}" +
                        "value${CSV_SEPARATOR}" +
                        "time_from_meal${CSV_SEPARATOR}" +
                        "comment")
                writer.flush()
                measurements?.forEach { measurement ->
                    val (hoursFromMeal, minutesFromMeal) = minutesToHoursAndMinutes(measurement.minutesFromMeal)
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(measurement.timestamp)}${CSV_SEPARATOR}" +
                        "${floatToCommaString(measurement.value)}${CSV_SEPARATOR}" +
                        "${hoursFromMeal}:${minutesFromMeal}${CSV_SEPARATOR}" +
                        measurement.comment
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
)  = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine("#insulin")
                writer.appendLine(
                    "date${CSV_SEPARATOR}" +
                            "dose${CSV_SEPARATOR}" +
                            "comment"
                )
                writer.flush()
                insulinInjections?.forEach { insulinInjection ->
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(insulinInjection.timestamp)}${CSV_SEPARATOR}" +
                                "${insulinInjection.dose}${CSV_SEPARATOR}" +
                                insulinInjection.comment
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
)  = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine("#meals")
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

suspend fun writeOthersToDownloadsCSV(
    context: Context,
    others: List<Other>?,
    fileUri: Uri,
) = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openOutputStream(fileUri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                writer.appendLine("Sep=${CSV_SEPARATOR}")
                writer.appendLine("#others")
                writer.appendLine("date${CSV_SEPARATOR}" +
                        "comment")
                writer.flush()
                others?.forEach { other ->
                    writer.appendLine(
                        "${longToLocalDateTimeStringWithTimezone(other.timestamp)}${CSV_SEPARATOR}" +
                                other.comment
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

enum class FileType {
    MEASUREMENTS,
    INSULIN,
    MEALS,
    OTHERS
}
suspend fun determineFileContent(context: Context, uri: Uri): FileType?  =
    withContext(Dispatchers.IO) {
    try {
        var type: FileType? = null
        var i = 0
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    when (i) {
                        0 -> {
                            if (!line.startsWith("Sep=") || line.length <= 4) {
                                throw Exception("Invalid CSV file format")
                            }
                        }
                        1 -> {
                            when (line.trim()) {
                                "#measurements" -> {
                                    type = FileType.MEASUREMENTS
                                }
                                "#insulin" -> {
                                    type = FileType.INSULIN
                                }
                                "#meals" -> {
                                    type = FileType.MEALS
                                }
                                "#others" -> {
                                    type = FileType.OTHERS
                                }
                            }
                        }
                        else -> {
                            return@forEachLine
                        }
                    }
                    i++
                }
            }
        }
        type
    } catch (e: Exception) {
        Log.e("readFile", "Error reading file", e)
        throw e
    }
}

suspend fun readMeasurementsFromCSV(context: Context, uri: Uri):
        List<BloodSugarMeasurement> =
    withContext(Dispatchers.IO) {
    var i = 0
    var separator = CSV_SEPARATOR
    val measurements = mutableListOf<BloodSugarMeasurement>()
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.forEachLine { line ->
                when (i) {
                    0 -> if (line.startsWith("Sep=") || line.length > 4) {
                        separator = line[4]
                        Log.d("readMeasurementsFromCSV", "Separator: $separator")
                    }
                    1,2 -> {
                        Log.d("readMeasurementsFromCSV", "Skipping header lines")
                    }
                    else -> {
                        val fields = line.split(separator)
                        if (fields.size < 2) {
                            throw Exception("$i")
                        }
                        var minutesFromMeal = 0
                        if (fields.size == 3) {
                            minutesFromMeal = fields[2].split(":")[1].toInt()
                            minutesFromMeal += fields[2].split(":")[0].toInt() * 60
                        }

                        var comment = ""
                        if (fields.size > 3) {
                            comment = fields[3]
                        }
                        val measurement = BloodSugarMeasurement(
                            timestamp = localDateTimeStringWithTimezoneToLong(fields[0]),
                            value = commaStringToFloat(fields[1]),
                            minutesFromMeal = minutesFromMeal,
                            afterMeal = minutesFromMeal > 0,
                            comment = comment
                        )
                        measurements.add(measurement)
                    }
                }
                i++
            }
        }
    }
    measurements
}

suspend fun readInsulinInjectionsFromCSV(context: Context, uri: Uri):
        List<InsulinInjection> =
    withContext(Dispatchers.IO) {
        var i = 0
        var separator = CSV_SEPARATOR
        val insulinInjections = mutableListOf<InsulinInjection>()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    when (i) {
                        0 -> if (line.startsWith("Sep=") || line.length > 4) {
                            separator = line[4]
                        }
                        1,2 -> {
                        }
                        else -> {
                            val fields = line.split(separator)
                            if (fields.size < 2) {
                                throw Exception("$i")
                            }

                            var comment  = ""

                            if (fields.size > 2) {
                                comment = fields[2]
                            }

                            val insulinInjection = InsulinInjection(
                                timestamp = localDateTimeStringWithTimezoneToLong(fields[0]),
                                dose = fields[1].toInt(),
                                comment = comment
                            )
                            insulinInjections.add(insulinInjection)
                        }
                    }
                    i++
                }
            }
        }
        insulinInjections
    }

suspend fun readMealsFromCSV(context: Context, uri: Uri):
        List<Meal> =
    withContext(Dispatchers.IO) {
        var i = 0
        var separator = CSV_SEPARATOR
        val meals = mutableListOf<Meal>()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    when (i) {
                        0 -> if (line.startsWith("Sep=") || line.length > 4) {
                            separator = line[4]
                        }
                        1,2 -> {
                        }
                        else -> {
                            val fields = line.split(separator)
                            if (fields.size < 4) {
                                throw Exception("$i")
                            }
                            val meal = Meal(
                                timestamp = localDateTimeStringWithTimezoneToLong(fields[0]),
                                calories = fields[1].toInt(),
                                carbohydrates = fields[2].toInt(),
                                comment = fields[3]
                            )
                            meals.add(meal)
                        }
                    }
                    i++
                }
            }
        }
        meals
    }


suspend fun readOthersFromCSV(context: Context, uri: Uri):
        List<Other> =
    withContext(Dispatchers.IO) {
        var i = 0
        var separator = CSV_SEPARATOR
        val others = mutableListOf<Other>()
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.forEachLine { line ->
                    when (i) {
                        0 -> if (line.startsWith("Sep=") || line.length > 4) {
                            separator = line[4]
                        }
                        1,2 -> {
                        }
                        else -> {
                            val fields = line.split(separator)
                            if (fields.size < 2) {
                                throw Exception("$i")
                            }
                            val other = Other(
                                timestamp = localDateTimeStringWithTimezoneToLong(fields[0]),
                                comment = fields[1]
                            )
                            others.add(other)
                        }
                    }
                    i++
                }
            }
        }
        others
    }
