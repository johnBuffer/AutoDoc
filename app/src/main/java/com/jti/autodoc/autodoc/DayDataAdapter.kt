package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView

class DayDataAdapter(private val track: Track, private var mContext: Context) :
    ArrayAdapter<DayDataModel>(mContext, R.layout.view_day, track.days)
{
    internal class ViewHolderItem {
        var medocView : LinearLayout? = null
        var dayNameView : TextView? = null
        var medocCountView : TextView? = null
        var button : Button? = null
    }

    private fun getMedocView(day : DayDataModel, medoc : MedocData, parent: ViewGroup) : View
    {
        // Initialize views to be inflated
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.view_medoc, parent, false)
        val timeView = rowView.findViewById(R.id.medocTime) as TextView
        val descriptionView = rowView.findViewById(R.id.medocDescription) as TextView
        timeView.text = medoc.time
        descriptionView.text = medoc.description

        // Pops a time picker to change the medoc's time
        timeView.setOnClickListener {
            PopUpUtils.getTimePicker(context) { selectedHour, selectedMinute ->
                medoc.setTime(selectedHour, selectedMinute)
                notifyDataSetChanged()
            }
        }

        // Pops a text input dialog to change medoc's description
        descriptionView.setOnClickListener {
            PopUpUtils.getTextDialog(context, "Enter description", descriptionView.text.toString(), "") { description : String ->
                medoc.description = description
                notifyDataSetChanged()
            }
        }

        // Pops a confirmation dialog to remove the current medoc
        descriptionView.setOnLongClickListener {
            PopUpUtils.getConfirmationPopUp(mContext, "Remove \"" + medoc.description + "\" ?",
                {day.removeMedoc(medoc) ; notifyDataSetChanged()},
                {}
            )
            true
        }

        return rowView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val viewHolder: ViewHolderItem
        var result = convertView

        if (result == null)
        {
            viewHolder = ViewHolderItem()
            // Initialize views to be inflated
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            result = inflater.inflate(R.layout.view_day, parent, false)

            viewHolder.medocView = result.findViewById(R.id.medocList)
            viewHolder.dayNameView = result.findViewById(R.id.dayName)
            viewHolder.medocCountView = result.findViewById(R.id.medocCount)
            viewHolder.button = result.findViewById(R.id.addMedocButton) as Button

            result?.tag = viewHolder
        }
        else
        {
            viewHolder = result.tag as ViewHolderItem
        }

        // Initialize views to be inflated
        val currentDay = track.days[position]

        // Sort medocs by time
        currentDay.sortMedocs()

        // Set day's name and add removal dialog
        val dayID = currentDay.dayID.toString()
        viewHolder.dayNameView!!.text = "Day $dayID"
        viewHolder.dayNameView!!.setOnLongClickListener {
            PopUpUtils.getConfirmationPopUp(mContext, "Remove Day $dayID ?",
                {track.removeDay(currentDay) ; notifyDataSetChanged()},
                {}
            )
            true
        }

        // Set the counter view
        viewHolder.medocCountView!!.text = currentDay.medocs.size.toString()

        // Add medocs to the current day
        viewHolder.medocView!!.removeAllViews()
        for (medoc : MedocData in currentDay.medocs)
        {
            viewHolder.medocView!!.addView(getMedocView(currentDay, medoc, parent))
        }

        // Add the "add" button to add a new medoc
        viewHolder.button!!.setOnClickListener {
            PopUpUtils.getTimePicker(context) { selectedHour, selectedMinute ->
                PopUpUtils.getTextDialog(context, "Enter description", "", "") { description : String ->
                    val newMedoc = MedocData(description, "")
                    newMedoc.setTime(selectedHour, selectedMinute)
                    currentDay.addMedoc(newMedoc)
                    notifyDataSetChanged()
                }
            }
        }

        return result!!
    }
}
