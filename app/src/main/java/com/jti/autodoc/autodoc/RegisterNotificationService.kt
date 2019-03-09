package com.jti.autodoc.autodoc
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.support.v4.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver()
{
    companion object {
        const val NEW_MEDOC = "new_medoc"
        //val ALUM_SCREEN_OFF = "screenOff"
        //private val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = getActivity(context, 0, notificationIntent, FLAG_ONE_SHOT)

        val notificationId = MainActivity.NOTIFICATION_ID++
        with(NotificationManagerCompat.from(context)) {
            val description = intent.extras.getString("medoc_description")
            notify(notificationId, NotificationBuilder.getMedocNotification(context, pendingIntent, "It's time !", description))
        }
    }
}