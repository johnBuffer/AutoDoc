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

    var days = ArrayList<DayDataModel>(0)
    lateinit var adapter : DayDataAdapter

    val SAVE_FILE_NAME = "data.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        println("Days: " + days.size.toString())

        adapter = DayDataAdapter(days, this)


        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.dayList)
        listView.adapter = adapter
    }

    override fun onStop() {
        super.onStop()

        var jsonData = dataToJson()
        val filename = SAVE_FILE_NAME
        val outputStream: FileOutputStream
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE)
            outputStream.write(jsonData.toString().toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("Sizeof save: " + jsonData.toString().toByteArray().size.toString())
    }

    fun onClick(view: View)
    {
        var newDay = DayDataModel(days.size+1)
        days.add(newDay)
        adapter.notifyDataSetChanged()
    }

    private fun dataToJson() : JSONObject
    {
        var root  = JSONObject()
        var dayArray = JSONArray()

        for (day : DayDataModel in days)
        {
            var dayData = JSONObject()
            var medocArray = JSONArray()

            for (medoc : MedocData in day.medocs)
            {
                var medocData = JSONObject()
                medocData.put("description", medoc.name)
                medocData.put("time", medoc.time)
                medocArray.put(medocData)
            }

            dayData.put("id", day.dayID)
            dayData.put("medocs", medocArray)

            dayArray.put(dayData)
        }

        root.put("days", dayArray)

        return root
    }



    private fun loadData() = try
    {
        var data = FileUtils.getStringFromFile(SAVE_FILE_NAME, this)

        var jsonData = JSONObject(data)

        var daysArray = jsonData.getJSONArray("days")

        for (i in 0 until daysArray.length())
        {
            var obj = daysArray.getJSONObject(i)
            var newDay = DayDataModel(obj.getInt("id"))
            var medocsArray = obj.getJSONArray("medocs")
            for (j in 0 until medocsArray.length())
            {
                var medocJson = medocsArray.getJSONObject(j)
                newDay.addMedoc(medocJson.getString("description"), medocJson.getString("time"))
            }

            days.add(newDay)
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
}
