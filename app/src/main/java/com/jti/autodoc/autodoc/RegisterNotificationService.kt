package com.jti.autodoc.autodoc
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver()
{
    companion object {
        const val NEW_MEDOC = "new_medoc"
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = getActivity(context, 0, notificationIntent, FLAG_ONE_SHOT)

        val notificationId = MainActivity.NOTIFICATION_ID++
        with(NotificationManagerCompat.from(context)) {
            val description = intent.extras.getString("medoc_description")
            val track = intent.extras.getString("medoc_track")
            val color = intent.extras.getString("track_color")

            val onIntent = Intent(context.applicationContext, AlarmUpdateService::class.java)
            onIntent.putExtra("notificationId", notificationId)

            val pendingIntentAction = PendingIntent.getService(
                context.applicationContext,
                System.currentTimeMillis().toInt(),
                onIntent,
                PendingIntent.FLAG_ONE_SHOT)

            val notification = NotificationBuilder.getMedocNotification(context, pendingIntent, "It's time ! ($track)", description!!, color!!)
            val action = NotificationCompat.Action.Builder(R.drawable.notification_ok_img, "Done", pendingIntentAction).build()
            notification.addAction(action)

            notify(notificationId, notification.build())
        }
    }
}