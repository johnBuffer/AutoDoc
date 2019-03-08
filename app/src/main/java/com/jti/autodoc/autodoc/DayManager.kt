package com.jti.autodoc.autodoc

import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

data class MedocDataTime(val startOffset : Long, val description: String, var id: Int)
{

}

class DayManager
{
    val days = ArrayList<DayDataModel>()
    var startDate: String = "01/01/2019"

    fun fromJson(jsonData : JSONObject)
    {
        val daysArray = jsonData.getJSONArray("days")

        startDate = jsonData.getString("start")

        for (i in 0 until daysArray.length())
        {
            val obj = daysArray.getJSONObject(i)
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

    fun toJson() : JSONObject
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

    fun getAllMedocs(time : Long) : ArrayList<MedocDataTime>
    {
        val size = days.size
        val currentDay = (time / DateUtils.MS_PER_DAY)
        val currentDayInProgram = currentDay % size
        val timeInDay = time % DateUtils.MS_PER_DAY

        var timings = ArrayList<MedocDataTime>()

        println("Current day in program : $currentDayInProgram, day since start : $currentDay")

        for (i : Int in 0 until size)
        {
            val currentIndex = ((i + currentDayInProgram)%size).toInt()
            val day = days[currentIndex]
            if (day.medocs.size > 0)
            {
                println("Searching for medoc in day $currentIndex at timeInDay: $timeInDay (${DateUtils.millisTOTime(timeInDay.toInt())})")
                for (medoc : MedocData in day.medocs)
                {
                    val medocTime = DateUtils.timeToMillis(medoc.time)
                    if (medocTime > timeInDay || i>0)
                    {
                        println("Found medoc at " + medoc.time + " -> OK (Offset time : ${(currentDay + i)* DateUtils.MS_PER_DAY + medocTime})")
                        val offset = (currentDay + i)* DateUtils.MS_PER_DAY + medocTime
                        timings.add(MedocDataTime(offset, medoc.description, 0))
                    }
                }
            }
        }

        return timings
    }
}

