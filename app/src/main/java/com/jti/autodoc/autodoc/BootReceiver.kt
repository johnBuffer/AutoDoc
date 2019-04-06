package com.jti.autodoc.autodoc

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

class AlarmsBootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val startServiceIntent = Intent(context, AlarmUpdateService::class.java)
        context.startService(startServiceIntent)
    }
}