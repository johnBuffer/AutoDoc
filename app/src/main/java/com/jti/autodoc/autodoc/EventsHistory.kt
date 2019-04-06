package com.jti.autodoc.autodoc

import android.content.Context
import org.json.JSONObject

class EventsHistory(val context : Context)
{
    var data : JSONObject = JSONObject()

    fun loadFromFile()
    {
        val filename = MainActivity.SAVE_FILE_HISTORY_NAME
        data = JSONObject(FileUtils.getStringFromFile(filename, context))
    }

    fun checkHistoryPresence(time : Long)
    {
        val date = DateUtils.millisToDate(time)

        val year = data[date.year.toString()] as JSONObject?

        if (year != null)
        {
            val month = year[date.month.toString()] as JSONObject?
            if (month != null)
            {
                val day = month[date.day.toString()] as JSONObject?
                if (day != null)
                {
                    println("DAY FOUND")
                }
            }
            else
            {
                println("MONTH NOT FOUND")
            }
        }
        else
        {
            println("YEAR NOT FOUND")
        }
    }
}