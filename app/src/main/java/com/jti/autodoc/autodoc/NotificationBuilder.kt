package com.jti.autodoc.autodoc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat

class NotificationBuilder
{
    companion object {
        fun getMedocNotification(context : Context, pendingIntent : PendingIntent, title : String, description : String, color : String = "#FFFFFF") : Notification
        {
            val builder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.notification_img)
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))*/
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColorized(true)
                .setColor(Color.parseColor(color))

            return builder.build()
        }

        fun createNotificationChannel(context : Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.app_notif_channel)
                val descriptionText = context.getString(R.string.app_notif_channel)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("default", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}