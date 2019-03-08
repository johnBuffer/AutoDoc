package com.jti.autodoc.autodoc
import android.content.Context
import android.content.Intent
import android.content.BroadcastReceiver
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver()
{
    companion object {
        const val NEW_MEDOC = "new_medoc"
        //val ALUM_SCREEN_OFF = "screenOff"
        //private val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context, intent: Intent)
    {
        println("Broadcast received")
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_SHORT).show()
        /*val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = getActivity(context, 0, notificationIntent, FLAG_ONE_SHOT)

        val notificationId = MainActivity.NOTIF_ID++
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, NotificationBuilder.getMedocNotification(context, pendingIntent))
        }*/
    }
}