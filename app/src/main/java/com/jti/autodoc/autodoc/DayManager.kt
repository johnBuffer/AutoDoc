package com.jti.autodoc.autodoc

import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class DayManager
{
    val days = ArrayList<DayDataModel>()
    var startDate: String = ""

    fun fromJson(jsonData : JSONObject)
    {
        var daysArray = jsonData.getJSONArray("days")

        startDate = jsonData.getString("start")

        for (i in 0 until daysArray.length())
        {
            var obj = daysArray.getJSONObject(i)
            days.add(DayDataModel(obj))
        }
    }

    fun addDay()
    {
        days.add(DayDataModel(size()+1))
    }

    fun size() : Int
    {
        return days.size
    }

    private fun toJson() : JSONObject
    {
        val root  = JSONObject()
        val dayArray = JSONArray()

        root.put("start", startDate)

        for (day : DayDataModel in days)
        {
            dayArray.put(day.toJson())
        }

        root.put("days", dayArray)
        return root
    }

    override fun toString() : String
    {
        return toJson().toString()
    }

    fun removeDay(day : DayDataModel)
    {
        days.remove(day)
    }

    fun setDate(year : Int, month : Int, day : Int)
    {
        var monthStr = month.toString()
        if (month < 10)
            monthStr = "0$monthStr"

        var dayStr = day.toString()
        if (day < 10)
            dayStr = "0$dayStr"

        startDate = "$dayStr/$monthStr/$year"
    }
}