package com.jti.autodoc.autodoc

import java.text.SimpleDateFormat
import java.util.*

class Date(val day : Int, val month : Int, val year : Int)
{
    override fun toString() : String
    {
        return DateUtils.getPrettyDate(year, month, day)
    }
}

class DateUtils
{
    companion object {
        const val MS_PER_DAY = 24*3600*1000
        const val MS_PER_MINUTE = 60000
        const val MS_PER_HOUR = 60* MS_PER_MINUTE

        fun dateToMillis(date : String) : Long
        {
            val arr = date.split('/')

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, arr[2].toInt())
            cal.set(Calendar.MONTH, arr[1].toInt()-1)
            cal.set(Calendar.DAY_OF_MONTH, arr[0].toInt())
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)

            return cal.timeInMillis
        }

        fun timeToMillis(time : String) : Long
        {
            val arr = time.split(':')

            return arr[0].toLong()* MS_PER_HOUR + arr[1].toLong() * MS_PER_MINUTE
        }

        fun millisTOTime(millis : Int) : String
        {
            val hour = millis / MS_PER_HOUR
            val minute = (millis - hour* MS_PER_HOUR) / MS_PER_MINUTE

            return "$hour:$minute"
        }

        fun millisToDate(millis : Long) : Date
        {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis

            return Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR))
        }

        fun getPrettyDate(year : Int, month : Int, day : Int) : String
        {
            var monthStr = month.toString()
            if (month < 10)
                monthStr = "0$monthStr"

            var dayStr = day.toString()
            if (day < 10)
                dayStr = "0$dayStr"

            return "$dayStr/$monthStr/$year"
        }
    }
}