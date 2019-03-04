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




class MedocDataAdapter(private val dataSet: MutableList<MedocData>, internal var mContext: Context) :
    ArrayAdapter<MedocData>(mContext, R.layout.view_day, dataSet), View.OnClickListener
{
    override fun onClick(v: View) {

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.view_medoc, parent, false)
        val timeView = rowView.findViewById(R.id.medocTime) as TextView
        val descriptionView = rowView.findViewById(R.id.medocDescription) as TextView
        timeView.text = dataSet[position].time.toString()
        descriptionView.text = dataSet[position].name
        // change the icon for Windows and iPhone

        return rowView
    }
}
