package com.example.sokerihiiri.utils

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