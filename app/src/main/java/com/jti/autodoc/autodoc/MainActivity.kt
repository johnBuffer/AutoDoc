package com.jti.autodoc.autodoc

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import java.io.*
import android.app.AlarmManager
import android.support.v4.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.content.Intent
import java.util.*
import android.R.attr.x




class MainActivity : Activity() {
    var dayManager = DayManager()
    lateinit var adapter : DayDataAdapter

    companion object {
        const val SAVE_FILE_NAME = "data.json"
    }

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

        val notifyIntent = Intent(this, Receiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            NotificationIntentService.NOTIFICATION_REQUEST,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        println("OK")

        val cal = Calendar.getInstance()
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR, 19)
        cal.set(Calendar.MINUTE, 10)

        /*val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, cal.timeInMillis,
            (1000 * 60 * 60 * 24).toLong(), pendingIntent
        )*/
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
}
