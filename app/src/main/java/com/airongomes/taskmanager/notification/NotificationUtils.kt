package com.airongomes.taskmanager.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.airongomes.taskmanager.MainActivity
import com.airongomes.taskmanager.R

/**
 * Send Notification is an extension of NotificationManager
 * It is responsible to build the notification
 * @param message, message text
 * @param context, Application context
 */

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(message: String, context: Context){

//    // When the notification is clicked open the activity
//    val contentIntent = Intent(context, MainActivity::class.java)
//
//    // Execute the content intent even if the app isn't open
//    val contentPendingIntent = PendingIntent.getActivity(
//            context,
//            NOTIFICATION_ID,
//            contentIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT // This will replace the current notification
//    )

    // Build the Notification
    val builder = NotificationCompat.Builder(context,
            context.getString(R.string.task_notification_channel_id)
    )
            .setSmallIcon(R.drawable.app_icon_notification)
            .setContentTitle(context.getString(R.string.task_notification_title))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            //.setContentIntent(contentPendingIntent)
            //.setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Cancel the notification identified by it id
 * @param notification_id, task id used to identify the notification
 */
fun NotificationManager.cancelNotificationId(notification_id: Long){
    cancel(notification_id.toInt())
}

/**
 * Cancel the notifications previously showed
 */
fun NotificationManager.cancelNotifications(){
    cancelAll()
}
