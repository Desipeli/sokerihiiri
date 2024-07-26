package com.example.sokerihiiri.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.sokerihiiri.R
import kotlin.random.Random

class InsulinNotificationService(
    private val context: Context,
) {
    // https://medium.com/@kathankraithatha/notifications-with-jetpack-compose-3302f27e1348

    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    fun showInsulinNotification(){
        val notification= NotificationCompat.Builder(context,"sokerihiiri_notification")
            .setContentTitle("Sokerihiiri")
            .setContentText("Muista insuliini!")
            .setSmallIcon(R.drawable.vaccine)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}