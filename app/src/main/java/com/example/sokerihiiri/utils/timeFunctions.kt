package com.example.sokerihiiri.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

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