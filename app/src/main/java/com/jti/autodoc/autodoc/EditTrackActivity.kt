package com.jti.autodoc.autodoc;

import android.app.Activity;
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.json.JSONObject
import android.content.Intent
import android.graphics.Color


class EditTrackActivity : Activity() {
    var track = Track()
    var position : Int = 0

    private var trackData = JSONObject()
    lateinit var adapter : DayDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent != null)
        {
            trackData = JSONObject(this.intent.getStringExtra("trackData"))
            position = this.intent.getIntExtra("trackPosition", 0)
            track.fromJson(trackData)
        }

        setContentView(R.layout.activity_edit_track)

        adapter = DayDataAdapter(track, this)

        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.dayList)
        listView.adapter = adapter

        val timeView = findViewById<TextView>(R.id.startDate)
        timeView.text = track.startDate
        timeView.setOnClickListener {
            PopUpUtils.getCalendarPicker(this) { year: Int, month: Int, day: Int ->
                track.setDate(year, month + 1, day)
                timeView.text = track.startDate
            }
        }

        val trackNameView = findViewById<TextView>(R.id.trackName)
        trackNameView.text = track.name
        trackNameView.setOnClickListener {
            PopUpUtils.getTextDialog(this,"Enter track name", track.name) { text : String ->
                track.name = text
                trackNameView.text = track.name
            }
        }

        val button = findViewById<Button>(R.id.addDay)
        button.setOnClickListener {
            this.onClick(button)
        }

        val color = findViewById<TextView>(R.id.colorPicker)
        color.setBackgroundColor(Color.parseColor(track.color))
        color.setOnClickListener {
            val colorIntent = Intent(this, ChooseColorActivity::class.java)
            startActivityForResult(colorIntent, 0)
        }
    }

    fun onClick(view: View)
    {
        track.addDay()
        adapter.notifyDataSetChanged()
    }

    // This method will be invoked when user click android device Back menu at bottom.
    override fun onBackPressed()
    {
        val intent = Intent()
        intent.putExtra("trackData", track.toString())
        intent.putExtra("trackPosition", position)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode : Int, resultCode : Int, dataIntent : Intent)
    {
        val colorCode = dataIntent.getStringExtra("color")

        if (colorCode.length > 0) {
            track.color = colorCode
            val color = findViewById<TextView>(R.id.colorPicker)
            color.setBackgroundColor(Color.parseColor(track.color))
        }
    }
}
