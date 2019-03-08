package com.jti.autodoc.autodoc

import java.util.*

class DateUtils
{
    companion object {
        fun dateToMillis(date : String) : Long
        {
            val arr = date.split('/')

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, arr[2].toInt())
            cal.set(Calendar.MONTH, arr[1].toInt()-1)
            cal.set(Calendar.DAY_OF_MONTH, arr[0].toInt())

            return cal.timeInMillis
        }

        fun timeToMillis(time : String) : Long
        {
            val arr = time.split(':')

            return (arr[0].toLong()*60 + arr[1].toLong())*60000
        }
    }
}