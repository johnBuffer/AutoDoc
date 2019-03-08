package com.jti.autodoc.autodoc

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


class NotificationBuilder
{
    companion object {
        fun getMedocNotification(context : Context, pendingIntent : PendingIntent) : Notification
        {
            val builder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            return builder.build()
        }

        fun getForeGroundNotification(context: Context, pendingIntent: PendingIntent, channel_id : String) : Notification
        {
            return NotificationCompat.Builder(
                context,
                channel_id
            )
                .setOngoing(true)
                .setSmallIcon(R.drawable.notify_panel_notification_icon_bg)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build()
        }
    }
}