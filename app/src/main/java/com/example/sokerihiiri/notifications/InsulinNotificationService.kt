package com.example.sokerihiiri.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.sokerihiiri.MainActivity
import com.example.sokerihiiri.R
import kotlin.random.Random

class InsulinNotificationService(
    private val context: Context,
) {
    // https://medium.com/@kathankraithatha/notifications-with-jetpack-compose-3302f27e1348

    private val notificationManager=context.getSystemService(NotificationManager::class.java)
    fun showInsulinNotification(){

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification= NotificationCompat.Builder(context,"sokerihiiri_notification")
            .setContentTitle("Sokerihiiri")
            .setContentText("Muista insuliini!")
            .setSmallIcon(R.drawable.insulin_outlines)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
}