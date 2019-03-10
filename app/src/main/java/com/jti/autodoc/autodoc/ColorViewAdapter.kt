package com.jti.autodoc.autodoc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColorViewAdapter(private val colors: ArrayList<ChooseColorActivity.ColorView>, private val activity: ChooseColorActivity) :
    ArrayAdapter<ChooseColorActivity.ColorView>(activity, R.layout.view_day, colors)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val color = inflater.inflate(R.layout.view_color, parent, false)
        val textView = color.findViewById<TextView>(R.id.colorView)

        val currentColor = colors[position]
        textView.setBackgroundColor(Color.parseColor(currentColor.code))

        textView.setOnClickListener {
            activity.returnResult(currentColor.code)
        }

        return color
    }
}