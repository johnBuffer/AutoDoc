package com.jti.autodoc.autodoc

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import android.app.TimePickerDialog
import java.util.*
import android.content.DialogInterface

class DayDataAdapter(private val dayManager: DayManager, var mContext: Context) :
    ArrayAdapter<DayDataModel>(mContext, R.layout.view_day, dayManager.days)
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
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    medoc.setTime(selectedHour, selectedMinute)
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
        var currentDay = dayManager.days[position]

        currentDay.sortMedocs()

        val dayID = currentDay.dayID.toString()
        var dayName = dayView.findViewById<TextView>(R.id.dayName)
        dayName.text = "Day $dayID"
        dayName.setOnLongClickListener(View.OnLongClickListener {
            PopUpUtils.getConfirmationPopUp("Remove Day $dayID ?",
                {
                    dayManager.removeDay(currentDay)
                    notifyDataSetChanged()
                },
                {},
                mContext
            )
            true
        })

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
