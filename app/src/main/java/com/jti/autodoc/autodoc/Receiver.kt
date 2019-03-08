package com.jti.autodoc.autodoc

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v4.app.NotificationManagerCompat
import android.app.PendingIntent
import android.app.IntentService
import android.app.Notification


class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("LOOOOOOOOOOOOOOOOOOOOOOOL")
        val intent1 = Intent(context, NotificationIntentService::class.java)
        context.startService(intent1)
    }
}

class NotificationIntentService : IntentService("NotificationIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        val builder = Notification.Builder(this)
        builder.setContentTitle("My Title")
        builder.setContentText("This is the Body")
        //builder.setSmallIcon(R.drawable.whatever)
        val notifyIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent)
        val notificationCompat = builder.build()
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(NOTIFICATION_ID, notificationCompat)
    }

    companion object {
        private val NOTIFICATION_ID = 3
        val NOTIFICATION_REQUEST = 2
    }
}