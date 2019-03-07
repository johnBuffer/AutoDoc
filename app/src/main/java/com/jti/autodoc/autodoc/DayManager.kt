package com.jti.autodoc.autodoc

import org.json.JSONArray
import org.json.JSONObject

class DayManager
{
    val days = ArrayList<DayDataModel>()

    fun fromJson(jsonData : JSONObject)
    {
        var daysArray = jsonData.getJSONArray("days")

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
        var root  = JSONObject()
        var dayArray = JSONArray()

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
}