package com.jti.autodoc.autodoc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import java.io.*
import android.content.Intent
import android.app.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.app.PendingIntent
import android.app.AlarmManager
import android.widget.Toast
import org.json.JSONArray


class MainActivity : Activity() {
    var dayManager = DayManager()
    lateinit var adapter : DayDataAdapter

    val pendingIds = ArrayList<MedocDataTime>()

    companion object {
        const val SAVE_FILE_NAME = "data.json"
        var NOTIF_ID = 0
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        adapter = DayDataAdapter(dayManager, this)

        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.dayList)
        listView.adapter = adapter

        val timeView = findViewById<TextView>(R.id.startDate)
        timeView.text = dayManager.startDate
        timeView.setOnClickListener {
            PopUpUtils.getCalendarPicker(this) { year: Int, month: Int, day: Int ->
                dayManager.setDate(year, month + 1, day)
                timeView.text = dayManager.startDate
            }
        }
    }

    private fun updateAlarms()
    {
        println("Start")
        removeAllAlarms()
        createNotificationChannel()

        var requestCodeId = 0

        val startTime = DateUtils.dateToMillis(dayManager.startDate)
        val currentTime = System.currentTimeMillis()
        val offset = currentTime - startTime

        println("Current time: $currentTime")

        val medocsTiming = dayManager.getAllMedocs(offset)

        for (medoc : MedocDataTime in medocsTiming)
        {
            val onIntent = Intent(this, AlarmReceiver::class.java)
            onIntent.action = AlarmReceiver.NEW_MEDOC
            onIntent.putExtra("medoc_description", medoc.description)

            val pendingIntent =
                PendingIntent.getBroadcast(this, requestCodeId++, onIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime + medoc.startOffset, pendingIntent)

            medoc.id = requestCodeId
            pendingIds.add(medoc)
            //println("Setting alarm for ${DateUtils.millisToDate(startTime + medoc.startOffset)}")
        }
    }

    override fun onStop() {
        super.onStop()

        updateAlarms()
        Toast.makeText(this, "Alarms have been updated", Toast.LENGTH_SHORT).show()

        val filename = SAVE_FILE_NAME
        val outputStream: FileOutputStream
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)

            val root = dayManager.toJson()
            val pendingArray = JSONArray()
            for (medoc : MedocDataTime in pendingIds)
            {
                val obj = JSONObject()
                obj.put("id", medoc.id)
                obj.put("description", medoc.description)
                pendingArray.put(obj)
            }

            root.put("pendingIds", pendingArray)

            outputStream.write(root.toString().toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onClick(view: View)
    {
        dayManager.addDay()
        adapter.notifyDataSetChanged()
    }

    private fun removeAllAlarms()
    {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (medoc : MedocDataTime in pendingIds)
        {
            val onIntent = Intent(this, AlarmReceiver::class.java)
            onIntent.action = AlarmReceiver.NEW_MEDOC
            onIntent.putExtra("medoc_description", medoc.description)

            val pendingIntent =
                PendingIntent.getBroadcast(this, medoc.id, onIntent, PendingIntent.FLAG_CANCEL_CURRENT)

            alarmManager.cancel(pendingIntent)
        }
    }

    private fun loadData() = try
    {
        val data = FileUtils.getStringFromFile(SAVE_FILE_NAME, this)

        val jsonData = JSONObject(data)

        val pendingArray = jsonData.getJSONArray("pendingIds")
        for (i in 0 until pendingArray.length())
        {
            val medocID = pendingArray.getJSONObject(i).getInt("id")
            val medocDescription = pendingArray.getJSONObject(i).getString("description")
            pendingIds.add(MedocDataTime(0, medocDescription, medocID))
        }

        dayManager.fromJson(jsonData)

    }
    catch (e: Exception) {
        e.printStackTrace()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_notif_channel)
            val descriptionText = getString(R.string.app_notif_channel)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("default", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
