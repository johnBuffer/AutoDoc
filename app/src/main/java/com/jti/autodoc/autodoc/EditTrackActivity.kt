package com.jti.autodoc.autodoc;

import android.app.Activity;
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import android.content.Intent



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
        dayManager.addDay()
        adapter.notifyDataSetChanged()
    }

    // This method will be invoked when user click android device Back menu at bottom.
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("Track name", "This data is returned when user click back menu in target activity.")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
