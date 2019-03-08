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


class MainActivity : Activity() {
    var dayManager = DayManager()
    lateinit var adapter : DayDataAdapter

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

        createNotificationChannel()

        val myIntent = Intent(this, RegisterNotificationService::class.java)
        startService(myIntent)
    }

    override fun onStop() {
        super.onStop()

        val filename = SAVE_FILE_NAME
        val outputStream: FileOutputStream
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(dayManager.toString().toByteArray())
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

    private fun loadData() = try
    {
        var data = FileUtils.getStringFromFile(SAVE_FILE_NAME, this)
        dayManager.fromJson(JSONObject(data))
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
