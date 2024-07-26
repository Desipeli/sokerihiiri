package com.example.sokerihiiri.notifications

import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleInsulinNotification(workManager: WorkManager, hours: Int, minutes: Int) {
    //https://medium.com/androiddevelopers/workmanager-periodicity-ff35185ff006
    val now = Calendar.getInstance()
    val notificationTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
        if (before(now)) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    var initialDelay = notificationTime.timeInMillis - now.timeInMillis

    if (initialDelay == 0L) {
        initialDelay = 24*60*60*1000
    }

    Log.d("SettingsViewModel", "Scheduling daily notification in $initialDelay milliseconds")

    val workRequest = OneTimeWorkRequestBuilder<InsulinRemainderWorker>()
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .addTag("insulin_remainder")
        .build()

    workManager.enqueueUniqueWork(
        "insulin_remainder",
        ExistingWorkPolicy.REPLACE,
        workRequest)
}

fun cancelInsulinNotification(workManager: WorkManager) {
    workManager.cancelUniqueWork("insulin_remainder")
}