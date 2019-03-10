package com.jti.autodoc.autodoc

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup


class MedocDataAdapter(private val dataSet: MutableList<MedocData>, internal var mContext: Context) :
    ArrayAdapter<MedocData>(mContext, R.layout.view_day, dataSet)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.view_medoc, parent, false)
        val timeView = rowView.findViewById(R.id.medocTime) as TextView
        val descriptionView = rowView.findViewById(R.id.medocDescription) as TextView
        timeView.text = dataSet[position].time
        descriptionView.text = dataSet[position].description

        return rowView
    }
}
