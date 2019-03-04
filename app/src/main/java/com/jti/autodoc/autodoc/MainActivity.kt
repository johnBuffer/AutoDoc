package com.jti.autodoc.autodoc

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ListView

class MainActivity : Activity() {

    var days = ArrayList<DayDataModel>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Create the adapter to convert the array to views
        val adapter = DayDataAdapter(days, this)
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.DayListView)
        listView.adapter = adapter
    }

    fun onClick(view: View)
    {
        var newDay = DayDataModel(days.size)
        newDay.addMedoc("Day " + newDay.dayID.toString(), 1200)
        days.add(newDay)
    }
}
