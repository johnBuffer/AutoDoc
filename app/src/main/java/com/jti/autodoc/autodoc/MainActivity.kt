package com.jti.autodoc.autodoc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import java.io.*
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Intent
import java.util.*
import android.R.attr.x
import android.app.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


class MainActivity : Activity() {
    var dayManager = DayManager()
    lateinit var adapter : DayDataAdapter

    companion object {
        const val SAVE_FILE_NAME = "data.json"
        const val CHANNEL_ID = 5
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
            PopUpUtils.getCalendarPicker(this) { year: Int, month : Int, day : Int ->
                dayManager.setDate(year, month+1, day)
                timeView.text = dayManager.startDate
            }
        }

        createNotificationChannel()

        val cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        cal.set(Calendar.YEAR, 2019)
        cal.set(Calendar.MONTH, Calendar.MARCH)
        cal.set(Calendar.DAY_OF_MONTH, 8)
        cal.set(Calendar.HOUR, 11)
        cal.set(Calendar.MINUTE, 47)

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, cal.timeInMillis, 1000, "Medoc notification",
            {
                val notificationId = 1
                var builder = NotificationCompat.Builder(this, getString(R.string.app_notif_channel))
                    .setSmallIcon(R.drawable.notification_icon_background)
                    .setContentTitle("My notification")
                    .setContentText("Much longer text that cannot fit one line...")
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(this)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(notificationId, builder.build())
                }
            },
            null)
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
            val descriptionText = getString(R.string.app_notif_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.app_notif_channel), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
