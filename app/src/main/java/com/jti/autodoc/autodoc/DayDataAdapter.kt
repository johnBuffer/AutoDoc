package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*


class DayDataAdapter(private val dataSet: MutableList<DayDataModel>, internal var mContext: Context) :
    ArrayAdapter<DayDataModel>(mContext, R.layout.view_day, dataSet)
{

    private fun getMedocView(medoc : MedocData, parent: ViewGroup) : View
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.view_medoc, parent, false)
        val timeView = rowView.findViewById(R.id.medocTime) as TextView
        val descriptionView = rowView.findViewById(R.id.medocDescription) as TextView
        timeView.text = medoc.time.toString()
        descriptionView.text = medoc.name

        return rowView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dayView = inflater.inflate(R.layout.view_day, parent, false)
        var medocView = dayView.findViewById(R.id.medocList) as LinearLayout
        var currentDay = dataSet[position]

        var nText = dayView.findViewById<TextView>(R.id.editText)
        nText.text = currentDay.medocs.size.toString()

        for (medoc : MedocData in currentDay.medocs)
        {
            medocView.addView(getMedocView(medoc, parent))
        }

        var button = dayView.findViewById(R.id.addMedocButton) as Button
        button.setOnClickListener {
            currentDay.addMedoc("New medoc", 0)
            notifyDataSetChanged()
        }


        return dayView
    }
}
