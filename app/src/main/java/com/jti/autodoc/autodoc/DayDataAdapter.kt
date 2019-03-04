package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.R.string.ok
import android.R.string.no
import android.R.attr.label
import android.widget.TextView
import android.support.v4.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView


class DayDataAdapter(private val dataSet: MutableList<DayDataModel>, internal var mContext: Context) :
    ArrayAdapter<DayDataModel>(mContext, R.layout.view_day, dataSet)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dayView = inflater.inflate(R.layout.view_day, parent, false)
        val medocView = dayView.findViewById(R.id.medocList) as ListView

        var currentDay = dataSet[position]
        var medocAdapter = MedocDataAdapter(currentDay.medocs, mContext)
        medocView.adapter = medocAdapter

        return dayView
    }
}
