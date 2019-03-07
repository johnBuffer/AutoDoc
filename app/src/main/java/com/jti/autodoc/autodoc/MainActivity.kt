package com.jti.autodoc.autodoc

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

class MainActivity : Activity() {
    var dayManager = DayManager()
    lateinit var adapter : DayDataAdapter

    companion object {
        const val SAVE_FILE_NAME = "data.json"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        adapter = DayDataAdapter(dayManager.days, this)

        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.dayList)
        listView.adapter = adapter
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
