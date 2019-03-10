package com.jti.autodoc.autodoc

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView

class ChooseColorActivity : Activity()
{

    data class ColorView(val name : String, val code : String)

    private val colors = ArrayList<ColorView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.color_chooser_layout)
        val colorListView = findViewById<ListView>(R.id.colorList)

        colors.add(ColorView("", "#a6c7ea"))
        colors.add(ColorView("", "#89bbe0"))
        colors.add(ColorView("", "#6899aa"))
        colors.add(ColorView("", "#a9dcd3"))
        colors.add(ColorView("", "#90d0b6"))
        colors.add(ColorView("", "#88bdab"))
        colors.add(ColorView("", "#b9ddad"))
        colors.add(ColorView("", "#bac9b2"))
        colors.add(ColorView("", "#f7f48d"))
        colors.add(ColorView("", "#dfe88d"))
        colors.add(ColorView("", "#f0ed8c"))
        colors.add(ColorView("", "#f2f1b0"))
        colors.add(ColorView("", "#cdab8f"))
        colors.add(ColorView("", "#fbdddd"))
        colors.add(ColorView("", "#f8b2bd"))
        colors.add(ColorView("", "#f59597"))
        colors.add(ColorView("", "#f9a485"))
        colors.add(ColorView("", "#f2a794"))
        colors.add(ColorView("", "#fbbe7d"))
        colors.add(ColorView("", "#ceb3d4"))
        colors.add(ColorView("", "#b4b3db"))
        colors.add(ColorView("", "#c0c0c0"))

        val adapter = ColorViewAdapter(colors, this)

        // Create the adapter to convert the array to views
        // Attach the adapter to a ListView
        val listView = findViewById<ListView>(R.id.colorList)
        listView.adapter = adapter
    }

    override fun onBackPressed()
    {
        returnResult("")
    }

    fun returnResult(code : String)
    {
        val intent = Intent()
        intent.putExtra("color", code)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}