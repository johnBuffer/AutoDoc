package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*

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
            PopUpUtils.getTimePicker(context) { selectedHour, selectedMinute ->
                medoc.setTime(selectedHour, selectedMinute)
                notifyDataSetChanged()
            }
        }

        descriptionView.setOnClickListener {
            PopUpUtils.getTextDialog(context, "Enter description", {description : String ->
                medoc.description = description
                notifyDataSetChanged()
            }, {})
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
            PopUpUtils.getConfirmationPopUp(mContext, "Remove Day $dayID ?",
                {dayManager.removeDay(currentDay) ; notifyDataSetChanged()},
                {}
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
