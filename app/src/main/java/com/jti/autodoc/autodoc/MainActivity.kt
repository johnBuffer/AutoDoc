package com.jti.autodoc.autodoc

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ListView

class MainActivity : Activity() {

    var days = ArrayList<DayDataModel>(0)
    lateinit var adapter : DayDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = DayDataAdapter(days, this)

        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.dayList)
        listView.adapter = adapter
    }

    fun onClick(view: View)
    {
        var newDay = DayDataModel(days.size)
        newDay.addMedoc("Day " + newDay.dayID.toString(), 1200)
        days.add(newDay)
        adapter.notifyDataSetChanged()
    }

    fun onDayClick(view: View)
    {
        var newDay = DayDataModel(days.size)
        newDay.addMedoc("Day " + newDay.dayID.toString(), 1200)
        days.add(newDay)
        adapter.notifyDataSetChanged()
    }
}
