package com.jti.autodoc.autodoc

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.support.v4.app.NotificationManagerCompat


class AlarmReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
        val intent = Intent(context, MainActivity.javaClass)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        val notificationId = MainActivity.NOTIF_ID++
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, NotificationBuilder.getMedocNotification(context, pendingIntent))
        }
    }
}