package com.example.sokerihiiri.utils

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
    // Poimitaan päivämäärä date:sta ja yhdistetään se ajan(h, min) kanssa. Palautetaan aika timestamppina
    val instant = Instant.ofEpochMilli(date)
    val datePart = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
    val dateTime = LocalDateTime.of(datePart, LocalDateTime.of(1970, 1, 1, hours, minutes).toLocalTime())
    val zonedDateTime = dateTime.atZone(ZoneId.systemDefault())
    return zonedDateTime.toInstant().toEpochMilli()
}

fun timestampToDateTimeString(timestamp: Long): String {
    // Muutetaan timestamp ihmisluettavaan muotoon
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}

fun minutesToHoursAndMinutes(minutes: Int): Pair<Int, Int> {
    // Muutetaan minuutit tunneiksi ja minuuteiksi
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return Pair(hours, remainingMinutes)
}

fun timestampToHoursAndMinutes(timestamp: Long): Pair<Int, Int> {
    // Otetaan tunnit ja minuutit timestampista
    val instant = Instant.ofEpochMilli(timestamp)
    val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val hours = dateTime.hour
    val minutes = dateTime.minute
    return Pair(hours, minutes)
}
fun longToLocalDateTimeStringWithTimezone(timeInMillis: Long): String {
    // Timestamp csv-tiedostoon tallennettavaksi merkkijonoksi
    val instant = Instant.ofEpochMilli(timeInMillis)
    val zoneId = ZoneId.systemDefault()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").withZone(zoneId)
    return formatter.format(instant)
}

fun localDateTimeStringWithTimezoneToLong(dateTimeString: String): Long {
    // csv-tiedostosta luettu merkkijono timestampiksi
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
    val zonedDateTime = ZonedDateTime.parse(dateTimeString, formatter)
    val instant = zonedDateTime.toInstant()
    return instant.toEpochMilli()
}

fun getTimestampRangeForTodayBefore(hours: Int, minutes: Int): Pair<Long, Long> {
    // Palautetaan kuluvan päivän alku ja valittu hetki timestamppina
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