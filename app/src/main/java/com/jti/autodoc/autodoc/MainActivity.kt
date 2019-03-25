package com.jti.autodoc.autodoc

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import org.json.JSONObject
import java.io.*
import android.content.Intent
import android.app.*
import android.app.PendingIntent
import android.app.AlarmManager
import android.widget.Toast
import org.json.JSONArray


class MainActivity : Activity() {
    private var pendingIds = ArrayList<MedocDataTime>()
    private var tracks = ArrayList<Track>()

    lateinit var adapter : TrackViewAdapter

    companion object {
        const val SAVE_FILE_PROGRAM_NAME = "data.json"
        const val SAVE_FILE_PENDINGS_NAME = "pendings.json"
        var NOTIFICATION_ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        adapter = TrackViewAdapter(tracks, this)

        setContentView(R.layout.activity_main)
        val listView = findViewById<ListView>(R.id.tracksView)
        listView.adapter = adapter

        NotificationBuilder.createNotificationChannel(this)
    }

    override fun onStop()
    {
        super.onStop()

        JsonUtils.writeJsonToFile(SAVE_FILE_PROGRAM_NAME, JsonUtils.tracksArrayToJson(tracks), this)

        AlarmUtils.updateAlarms(tracks, pendingIds, this)
        Toast.makeText(this, getString(R.string.updated_alarms_notice), Toast.LENGTH_SHORT).show()

        JsonUtils.writeJsonToFile(SAVE_FILE_PENDINGS_NAME, JsonUtils.pendingsArrayToJson(pendingIds), this)
    }

    // Load previous data from JSON file
    private fun loadData() = try
    {
        val dataProgram = FileUtils.getStringFromFile(SAVE_FILE_PROGRAM_NAME, this)
        val jsonDataProgram = JSONObject(dataProgram)
        tracks = JsonUtils.loadTracks(jsonDataProgram.getJSONArray("tracks"))

        val dataPendings = FileUtils.getStringFromFile(SAVE_FILE_PENDINGS_NAME, this)
        val jsonDataPendings = JSONObject(dataPendings)
        pendingIds = JsonUtils.loadPendingIDs(jsonDataPendings.getJSONArray("pendingIds"))
    }
    catch (e: Exception) {
        e.printStackTrace()
    }

    fun onClick(view: View)
    {
        PopUpUtils.getTextDialog(this, getString(R.string.new_track_name), "", getString(R.string.new_track_notice)) { name: String ->
            val newTrack = Track()
            newTrack.name = name
            PopUpUtils.getCalendarPicker(this) { year: Int, month: Int, day: Int ->
                newTrack.setDate(year, month + 1, day)
                tracks.add(newTrack)
                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, dataIntent : Intent)
    {
        val trackJsonData = JSONObject(dataIntent.getStringExtra("trackData"))
        val position = dataIntent.getIntExtra("trackPosition", 0)

        val updatedTrack = Track()
        updatedTrack.fromJson(trackJsonData)

        tracks[position] = updatedTrack
        adapter.notifyDataSetChanged()
    }
}
