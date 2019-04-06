package com.jti.autodoc.autodoc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmUtils
{
    companion object {
        private fun removeAllAlarms(pendings : ArrayList<MedocDataTime>, context : Context)
        {
            //println("Alarms to remove: ${pendingIds.size}")
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            for (medoc : MedocDataTime in pendings)
            {
                val onIntent = Intent(context.applicationContext, AlarmReceiver::class.java)
                onIntent.action = AlarmReceiver.NEW_MEDOC
                onIntent.putExtra("medoc_description", medoc.description)
                onIntent.putExtra("medoc_time", medoc.time)
                onIntent.putExtra("medoc_track", medoc.track)
                onIntent.putExtra("track_color", medoc.color)

                val pendingIntent = PendingIntent.getBroadcast(
                    context.applicationContext,
                    medoc.id,
                    onIntent,
                    PendingIntent.FLAG_NO_CREATE
                )

                try {
                    alarmManager.cancel(pendingIntent)
                } catch (e: Exception) {
                }
            }

            pendings.clear()
        }

        fun updateAlarms(tracks : ArrayList<Track>, pendings : ArrayList<MedocDataTime>, context : Context, history : EventsHistory)
        {
            removeAllAlarms(pendings, context)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val currentTime = System.currentTimeMillis()
            var requestCodeId = currentTime.toInt()

            for (track : Track in tracks)
            {
                val medocsTiming = track.getAllMedocs(currentTime)

                for (medoc: MedocDataTime in medocsTiming)
                {
                    // Create intent
                    val onIntent = Intent(context.applicationContext, AlarmReceiver::class.java)
                    onIntent.action = AlarmReceiver.NEW_MEDOC
                    onIntent.putExtra("medoc_data", medoc.toString())

                    medoc.id = requestCodeId++
                    val pendingIntent = PendingIntent.getBroadcast(
                        context.applicationContext,
                        medoc.id,
                        onIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

                    if (!history.checkHistoryPresence(medoc.time, medoc.getEventName()))
                    {
                        println("No history for ${DateUtils.millisToDate(medoc.time)}-> set Alarm")
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, medoc.time, pendingIntent)
                    }
                    else
                    {
                        println("History found for ${DateUtils.millisToDate(medoc.time)}-> no Alarm")
                    }

                    pendings.add(medoc)
                }
            }
        }
    }
}