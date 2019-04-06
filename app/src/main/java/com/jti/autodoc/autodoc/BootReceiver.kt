package com.jti.autodoc.autodoc

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

class AlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("BOOT")
        /*val startServiceIntent = Intent(context, AlarmUpdateService::class.java)
        context.startService(startServiceIntent)*/
    }
}