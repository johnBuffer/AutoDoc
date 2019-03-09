package com.jti.autodoc.autodoc;

import android.app.Activity;
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject

class EditDayActivity : Activity() {
    var dayManager = DayManager()
    private var trackData = JSONObject()
    lateinit var adapter : DayDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null)
        {
            trackData = JSONObject(this.intent.getStringExtra("trackData"))
            dayManager.fromJson(trackData)
        }

        setContentView(R.layout.edit_track)

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

    fun onClick(view: View)
    {
        dayManager.addDay()
        adapter.notifyDataSetChanged()
    }
}
