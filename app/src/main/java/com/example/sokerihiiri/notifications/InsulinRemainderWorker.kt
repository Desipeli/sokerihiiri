package com.example.sokerihiiri.notifications

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.sokerihiiri.repository.DataStoreManager
import com.example.sokerihiiri.utils.isTimestampToday
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class InsulinRemainderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workParameters: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val workManager: WorkManager
) : CoroutineWorker(context, workParameters){

    private val insulinNotificationService = InsulinNotificationService(context)

    override suspend fun doWork(): Result {
        try {
            Log.d("TAG", "Work started ")

            if (dataStoreManager.getInsulinDeadlineEnabled().first()) {
                Log.d("TAG", "Insulin notification enabled")

                val hours = dataStoreManager.getInsulinDeadlineHours().first()
                val minutes = dataStoreManager.getInsulinDeadlineMinutes().first()

                val latestInsulinTimestamp = dataStoreManager.getLatestInsulinTimestamp().first()

                if (latestInsulinTimestamp != null && isTimestampToday(latestInsulinTimestamp)) {
                    scheduleInsulinNotification(workManager, hours, minutes)
                } else {
                    insulinNotificationService.showInsulinNotification()
                    scheduleInsulinNotification(workManager, hours, minutes)
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }

    }
}