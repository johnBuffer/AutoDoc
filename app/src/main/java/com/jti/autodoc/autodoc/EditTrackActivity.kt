package com.jti.autodoc.autodoc;

import android.app.Activity;
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject

class EditTrackActivity : Activity() {
    var dayManager = Track()
    private var trackData = JSONObject()
    lateinit var adapter : DayDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null)
        {
            trackData = JSONObject(this.intent.getStringExtra("trackData"))
            dayManager.fromJson(trackData)
        }

        setContentView(R.layout.activity_edit_track)

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

        val button = findViewById<Button>(R.id.addDay)
        button.setOnClickListener {
            this.onClick(button)
        }
    }

    fun onClick(view: View)
    {
        println("CLICCCC")
        dayManager.addDay()
        adapter.notifyDataSetChanged()
    }
}
