package com.airongomes.taskmanager.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.airongomes.taskmanager.R

/**
 * Create the Notification Channel
 * @param chanelId, identify the channel
 * @param chanelName, name of the channel
 * @param context, Application context
 */
fun createNotificationChannel(chanelId: String, chanelName: String, context: Context){
    // Create the NotificationChannel on API 26+
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
                chanelId,
                chanelName,
                NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        channel.enableVibration(true)
        channel.description = context.getString(R.string.task_notification_description)
        // Register the channel with the system
        val notificationManager: NotificationManager =
                getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}