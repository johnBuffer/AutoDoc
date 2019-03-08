package com.jti.autodoc.autodoc

import org.json.JSONObject

data class MedocData(var description: String, var time: String)
{
    constructor(jsonData : JSONObject) : this(jsonData.getString("description"), jsonData.getString("time"))

    fun toJson() : JSONObject
    {
        var medocData = JSONObject()
        medocData.put("description", description)
        medocData.put("time", time)

        return medocData
    }

    fun setTime(hour : Int, minute : Int)
    {
        var hourStr = hour.toString()
        if (hour < 10)
            hourStr = "0$hourStr"

        var minutesStr = minute.toString()
        if (minute < 10)
            minutesStr = "0$minutesStr"

        time = "$hourStr:$minutesStr"
    }

    fun getTime(start_time : Long) : Long
    {
        return DateUtils.timeToMillis(time)
    }
}

