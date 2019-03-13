package com.jti.autodoc.autodoc

import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList

class MedocDataTime
{
    var startOffset : Long = 0
    var description: String = ""
    var track : String = ""
    var color : String = ""
    var id: Int = 0

    constructor(start: Long, description: String, track: String, color: String, id: Int)
    {
        this.id = id
        this.description = description
        this.startOffset = start
        this.track = track
        this.color = color
    }

    constructor(jsonData: JSONObject)
    {
        id = jsonData.getInt("id")
        description = jsonData.getString("description")
        startOffset = jsonData.getLong("startOffset")
        track = jsonData.getString("track")
        color = jsonData.getString("track_color")
    }

    fun toJson() :JSONObject
    {
        val obj = JSONObject()

        obj.put("id", id)
        obj.put("description", description)
        obj.put("startOffset", startOffset)
        obj.put("track", track)
        obj.put("track_color", color)

        return obj
    }
}

class Track
{
    val days = ArrayList<DayDataModel>()
    var startDate: String = "01/01/2019"
    var name = "New Track"
    var color = "#90d0b6"

    fun fromJson(jsonData : JSONObject)
    {
        name = jsonData.getString("name")
        startDate = jsonData.getString("start")
        color = jsonData.getString("color")

        val daysArray = jsonData.getJSONArray("days")
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

        root.put("name", name)
        root.put("start", startDate)
        root.put("color", color)

        val dayArray = JSONArray()
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
        startDate = DateUtils.getPrettyDate(year, month, day)
    }

    fun getAllMedocs(time : Long) : ArrayList<MedocDataTime>
    {
        val timings = ArrayList<MedocDataTime>()
        val size = days.size

        if (size == 0)
            return timings

        val currentDay = (time / DateUtils.MS_PER_DAY)
        val currentDayInProgram = currentDay % size
        val timeInDay = time % DateUtils.MS_PER_DAY

        //println("Current day in program : $currentDayInProgram, day since start : $currentDay")

        for (i : Int in 0 until size + 1)
        {
            val currentIndex = ((i + currentDayInProgram)%size).toInt()
            val day = days[currentIndex]
            if (day.medocs.size > 0)
            {
                //println("Searching for medoc in day $currentIndex at timeInDay: $timeInDay (${DateUtils.millisTOTime(timeInDay.toInt())})")
                for (medoc : MedocData in day.medocs)
                {
                    val medocTime = DateUtils.timeToMillis(medoc.time)
                    if (medocTime > timeInDay || i>0)
                    {
                        //println("Found medoc at " + medoc.time + " -> OK (Offset time : ${(currentDay + i)* DateUtils.MS_PER_DAY + medocTime})")
                        val offset = (currentDay + i)* DateUtils.MS_PER_DAY + medocTime
                        timings.add(MedocDataTime(offset, medoc.description, name, color, 0))
                    }
                }
            }
        }

        return timings
    }
}

