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
    private val pendingIds = ArrayList<MedocDataTime>()
    private val tracks = ArrayList<Track>()

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
    }

    private fun updateAlarms()
    {
        removeAllAlarms()

        val currentTime = System.currentTimeMillis()
        var requestCodeId = currentTime.toInt()

        for (track : Track in tracks)
        {
            //println("\nTrack ${track.name}")
            val startTime = DateUtils.dateToMillis(track.startDate)
            val offset = currentTime - startTime

            val medocsTiming = track.getAllMedocs(offset)

            for (medoc: MedocDataTime in medocsTiming)
            {
                val onIntent = Intent(applicationContext, AlarmReceiver::class.java)
                onIntent.action = AlarmReceiver.NEW_MEDOC
                onIntent.putExtra("medoc_description", medoc.description)
                onIntent.putExtra("medoc_track", medoc.track)
                onIntent.putExtra("track_color", medoc.color)

                medoc.id = requestCodeId++
                val pendingIntent = PendingIntent.getBroadcast(
                    applicationContext,
                    medoc.id,
                    onIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime + medoc.startOffset, pendingIntent)

                pendingIds.add(medoc)
                println("Setting alarm for ${DateUtils.millisToDate(startTime + medoc.startOffset)}")
            }
        }

        //println("Alarms added: ${pendingIds.size}")
    }

    override fun onStop()
    {
        super.onStop()

        val filename = SAVE_FILE_PROGRAM_NAME
        val outputStream: FileOutputStream
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)

            val root = JSONObject()
            val tracksArray = JSONArray()
            for (track : Track in tracks)
            {
                tracksArray.put(track.toJson())
            }
            root.put("tracks", tracksArray)

            outputStream.write(root.toString().toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        updateAlarms()
        Toast.makeText(this, getString(R.string.updated_alarms_notice), Toast.LENGTH_SHORT).show()

        val filenamePendings = SAVE_FILE_PENDINGS_NAME
        val outputStreamPendings: FileOutputStream
        try {
            outputStreamPendings = openFileOutput(filenamePendings, Context.MODE_PRIVATE)
            val root = JSONObject()
            val pendingArray = JSONArray()
            for (medoc : MedocDataTime in pendingIds)
            {
                pendingArray.put(medoc.toJson())
            }
            root.put("pendingIds", pendingArray)

            outputStreamPendings.write(root.toString().toByteArray())
            outputStreamPendings.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeAllAlarms()
    {
        //println("Alarms to remove: ${pendingIds.size}")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (medoc : MedocDataTime in pendingIds)
        {
            val onIntent = Intent(applicationContext, AlarmReceiver::class.java)
            onIntent.action = AlarmReceiver.NEW_MEDOC
            onIntent.putExtra("medoc_description", medoc.description)
            onIntent.putExtra("medoc_track", medoc.track)
            onIntent.putExtra("track_color", medoc.color)

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                medoc.id,
                onIntent,
                PendingIntent.FLAG_NO_CREATE
            )

            try {
                //println(pendingIntent.toString())
                alarmManager.cancel(pendingIntent)
            } catch (e: Exception) {
               //println("AlarmManager update was not canceled. $e")
            }
        }

        pendingIds.clear()
    }

    // Load previous data from JSON file
    private fun loadData() = try
    {
        val dataProgram = FileUtils.getStringFromFile(SAVE_FILE_PROGRAM_NAME, this)
        val jsonDataProgram = JSONObject(dataProgram)

        loadTracks(jsonDataProgram.getJSONArray("tracks"))

        val dataPendings = FileUtils.getStringFromFile(SAVE_FILE_PENDINGS_NAME, this)
        val jsonDataPendings = JSONObject(dataPendings)
        loadPendingIDs(jsonDataPendings.getJSONArray("pendingIds"))
    }
    catch (e: Exception) {
        e.printStackTrace()
    }

    private fun loadPendingIDs(array : JSONArray)
    {
        for (i in 0 until array.length())
        {
            val jsonData : JSONObject = array.getJSONObject(i)
            pendingIds.add(MedocDataTime(jsonData))
        }
    }

    private fun loadTracks(array : JSONArray)
    {
        for (i in 0 until array.length())
        {
            val currentTrack = array.getJSONObject(i)
            val newTrack = Track()
            newTrack.fromJson(currentTrack)
            tracks.add(newTrack)
        }
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
