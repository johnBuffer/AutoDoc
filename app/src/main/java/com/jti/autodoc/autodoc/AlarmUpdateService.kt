package com.jti.autodoc.autodoc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.json.JSONObject

class AlarmUpdateService : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        val dataProgram = FileUtils.getStringFromFile(MainActivity.SAVE_FILE_PROGRAM_NAME, this)
        val jsonDataProgram = JSONObject(dataProgram)
        val tracks = JsonUtils.loadTracks(jsonDataProgram.getJSONArray("tracks"))

        val dataPendings = FileUtils.getStringFromFile(MainActivity.SAVE_FILE_PENDINGS_NAME, this)
        val jsonDataPendings = JSONObject(dataPendings)
        val pendings = JsonUtils.loadPendingIDs(jsonDataPendings.getJSONArray("pendingIds"))

        // Pendings to JSON
        AlarmUtils.updateAlarms(tracks, pendings, this)
        JsonUtils.writeJsonToFile(MainActivity.SAVE_FILE_PENDINGS_NAME, JsonUtils.pendingsArrayToJson(pendings), this)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}