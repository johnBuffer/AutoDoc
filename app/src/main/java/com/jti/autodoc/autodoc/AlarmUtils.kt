package com.jti.autodoc.autodoc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.json.JSONArray
import org.json.JSONObject

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
                val startTime = DateUtils.dateToMillis(track.startDate)
                val offset = currentTime - startTime

                val medocsTiming = track.getAllMedocs(offset)

                for (medoc: MedocDataTime in medocsTiming)
                {
                    val onIntent = Intent(context.applicationContext, AlarmReceiver::class.java)
                    onIntent.action = AlarmReceiver.NEW_MEDOC
                    onIntent.putExtra("medoc_description", medoc.description)
                    onIntent.putExtra("medoc_track", medoc.track)
                    onIntent.putExtra("track_color", medoc.color)

                    medoc.id = requestCodeId++
                    val pendingIntent = PendingIntent.getBroadcast(
                        context.applicationContext,
                        medoc.id,
                        onIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

                    val alarmTime = startTime + medoc.startOffset

                    if (!history.checkHistoryPresence(alarmTime, medoc.description))
                    {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
                        history.addEventToHistory(alarmTime, medoc.description, "pending")
                    }

                    pendings.add(medoc)
                }
            }
        }
    }
}