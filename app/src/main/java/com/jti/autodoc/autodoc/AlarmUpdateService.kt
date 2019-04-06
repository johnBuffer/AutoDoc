package com.jti.autodoc.autodoc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import org.json.JSONObject
import android.app.NotificationManager



class AlarmUpdateService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        // Load context
        val dataProgram = FileUtils.getStringFromFile(MainActivity.SAVE_FILE_PROGRAM_NAME, this)
        val jsonDataProgram = JSONObject(dataProgram)
        val tracks = JsonUtils.loadTracks(jsonDataProgram.getJSONArray("tracks"))
        val dataPendings = FileUtils.getStringFromFile(MainActivity.SAVE_FILE_PENDINGS_NAME, this)
        val jsonDataPendings = JSONObject(dataPendings)
        val pendings = JsonUtils.loadPendingIDs(jsonDataPendings.getJSONArray("pendingIds"))
        val history = EventsHistory(this)
        history.loadFromFile()

        // Mark event as done
        val notificationId = intent.getIntExtra("notificationId", 0)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)

        // Retrieve medoc
        val medoc = MedocDataTime(JSONObject(intent.getStringExtra("medoc_data")))
        history.addEventToHistory(medoc.time, medoc.getEventName(), "OK")

        // Update alarms
        AlarmUtils.updateAlarms(tracks, pendings, this, history)
        // Pendings to JSON
        JsonUtils.writeJsonToFile(MainActivity.SAVE_FILE_PENDINGS_NAME, JsonUtils.pendingsArrayToJson(pendings), this)
        // History to JSON
        history.saveToFile()

        Toast.makeText(this, getString(R.string.updated_alarms_notice), Toast.LENGTH_SHORT).show()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}