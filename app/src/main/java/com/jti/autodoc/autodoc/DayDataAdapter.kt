package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import android.app.TimePickerDialog
import java.util.*

class DayDataAdapter(private val dataSet: MutableList<DayDataModel>, internal var mContext: Context) :
    ArrayAdapter<DayDataModel>(mContext, R.layout.view_day, dataSet)
{

    private fun getMedocView(medoc : MedocData, parent: ViewGroup) : View
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.view_medoc, parent, false)
        val timeView = rowView.findViewById(R.id.medocTime) as TextView
        val descriptionView = rowView.findViewById(R.id.medocDescription) as TextView
        timeView.text = medoc.time
        descriptionView.text = medoc.description

        timeView.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(mContext,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    medoc.time = "$selectedHour:$selectedMinute"
                    notifyDataSetChanged()
                }, hour, minute, true
            )
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        return rowView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dayView = inflater.inflate(R.layout.view_day, parent, false)
        var medocView = dayView.findViewById(R.id.medocList) as LinearLayout
        var currentDay = dataSet[position]

        var dayName = dayView.findViewById<TextView>(R.id.dayName)
        dayName.text = "Day " + currentDay.dayID.toString()

        var medocCountView = dayView.findViewById<TextView>(R.id.medocCount)
        medocCountView.text = currentDay.medocs.size.toString()

        for (medoc : MedocData in currentDay.medocs)
        {
            medocView.addView(getMedocView(medoc, parent))
        }

        var button = dayView.findViewById(R.id.addMedocButton) as Button
        button.setOnClickListener {
            currentDay.addMedoc("New medoc", "12:00")
            notifyDataSetChanged()
        }

        return dayView
    }
}
