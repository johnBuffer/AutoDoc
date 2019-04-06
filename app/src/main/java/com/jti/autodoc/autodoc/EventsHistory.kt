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

    fun checkHistoryPresence(time : Long, event : String) : Boolean
    {
        val date = DateUtils.millisToDate(time)

        val hasYear = data.has(date.year.toString())
        if (hasYear)
        {
            val year = data[date.year.toString()] as JSONObject
            val hasMonth = year.has(date.month.toString())
            if (hasMonth)
            {
                val month = year[date.month.toString()] as JSONObject
                val hasDay = month.has(date.day.toString())
                if (hasDay)
                {
                    val day = month[date.day.toString()] as JSONObject
                    return day.has(event)
                }
            }
        }

        return false
    }

    fun getHistoryValue(time : Long, event : String) : String
    {
        val date = DateUtils.millisToDate(time)

        val hasYear = data.has(date.year.toString())
        if (hasYear)
        {
            val year = data[date.year.toString()] as JSONObject
            val hasMonth = year.has(date.month.toString())
            if (hasMonth)
            {
                val month = year[date.month.toString()] as JSONObject
                val hasDay = month.has(date.day.toString())
                if (hasDay)
                {
                    val day = month[date.day.toString()] as JSONObject
                    if (day.has(event))
                    {
                        return day[event] as String
                    }
                }
            }
        }

        return ""
    }

    fun addEventToHistory(time : Long, event : String, value : String)
    {
        val date = DateUtils.millisToDate(time)

        // Check year for later access
        val hasYear = data.has(date.year.toString())
        if (!hasYear) {
            println("YEAR ${date.year} NOT FOUND -> CREATE")
            data.put(date.year.toString(), JSONObject())
        }
        val year = data[date.year.toString()] as JSONObject

        // Check month for later access
        val hasMonth = year.has(date.month.toString())
        if (!hasMonth) {
            println("MONTH ${date.month} NOT FOUND -> CREATE")
            year.put(date.month.toString(), JSONObject())
        }
        val month = year[date.month.toString()] as JSONObject

        // Check day for later access
        val hasDay = month.has(date.day.toString())
        if (!hasDay)
        {
            println("DAY ${date.day} NOT FOUND -> CREATE")
            month.put(date.day.toString(), JSONObject())
        }
        val day = month[date.day.toString()] as JSONObject

        // Set event value
        day.put(event, value)
    }

    fun saveToFile()
    {
        JsonUtils.writeJsonToFile(MainActivity.SAVE_FILE_HISTORY_NAME, data, context)
    }
}