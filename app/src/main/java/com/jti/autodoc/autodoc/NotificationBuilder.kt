package com.jti.autodoc.autodoc

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.support.v4.app.NotificationCompat

class NotificationBuilder
{
    companion object {
        fun getMedocNotification(context : Context, pendingIntent : PendingIntent, title : String, description : String) : Notification
        {
            val builder = NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.notification_icon_background)
                .setContentTitle(title)
                .setContentText(description)
                /*.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))*/
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            return builder.build()
        }
    }
}