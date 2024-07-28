package com.example.sokerihiiri.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun dateAndTimeToUTCLong(date: Long, hours: Int, minutes: Int): Long {
    // Poimitaan päivämäärä date:sta ja yhdistettän se ajan kanssa.
    val instant = Instant.ofEpochMilli(date)
    val datePart = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
    val dateTime = LocalDateTime.of(datePart, LocalDateTime.of(1970, 1, 1, hours, minutes).toLocalTime())
    // Muunnetaan utc 0 ja palautetaan millisekunnit
    val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
    return zonedDateTime.toInstant().toEpochMilli()
}

fun timestampToDateTimeString(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}

fun minutesToHoursAndMinutes(minutes: Int): Pair<Int, Int> {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return Pair(hours, remainingMinutes)
}

fun timestampToHoursAndMinutes(timestamp: Long): Pair<Int, Int> {
    val instant = Instant.ofEpochMilli(timestamp)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val hours = dateTime.hour
    val minutes = dateTime.minute
    return Pair(hours, minutes)
}

fun longToUtcTimestamp(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(Date(timeInMillis))
}

fun longToLocalDateTimeStringWithTimezone(timeInMillis: Long): String {
    val instant = Instant.ofEpochMilli(timeInMillis)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").withZone(zoneId)
    return formatter.format(instant)
}

fun localDateTimeStringWithTimezoneToLong(dateTimeString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    val zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter)
    val instant = zonedDateTime.toInstant()
    return instant.toEpochMilli()
}

fun getTimestampRangeForTodayBefore(hours: Int, minutes: Int): Pair<Long, Long> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val startTimestamp = calendar.timeInMillis

    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    val endTimestamp = calendar.timeInMillis

    return Pair(startTimestamp, endTimestamp)
}