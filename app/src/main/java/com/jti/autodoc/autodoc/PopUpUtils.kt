package com.jti.autodoc.autodoc

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import java.util.*
import android.text.InputType
import android.widget.EditText
import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.text.Selection
import android.text.Editable




class PopUpUtils
{
    companion object {
        fun getConfirmationPopUp(context : Context, question : String, lambdaYes: () -> Unit) : AlertDialog.Builder
        {
            return getConfirmationPopUp(context, question, lambdaYes, {})
        }

        fun getConfirmationPopUp(context : Context, question : String, lambdaYes: () -> Unit, lambdaNo: () -> Unit) : AlertDialog.Builder
        {
            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        lambdaYes()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        lambdaNo()
                    }
                }
            }

            val builder = AlertDialog.Builder(context)
            builder.setMessage(question).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show()

            return builder
        }

        fun getTimePicker(context : Context, lambda : (Int, Int) -> Unit) : TimePickerDialog
        {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)
            val timePicker: TimePickerDialog
            timePicker = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    lambda(selectedHour, selectedMinute)
                }, hour, minute, true
            )
            timePicker.setTitle("Select Time")
            timePicker.show()

            return timePicker
        }

        fun getTextDialog(context : Context, title : String, text : String, notice : String, lambdaYes: (String) -> Unit)
        {
            getTextDialog(context, title, text, notice, lambdaYes, {})
        }

        fun getTextDialog(context : Context, title : String, text : String, notice : String, lambdaYes: (String) -> Unit, lambdaNo: () -> Unit)
        {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogView = inflater.inflate(R.layout.text_input_dialog, null, false)

            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setView(dialogView)

            val input = dialogView.findViewById<EditText>(R.id.editText)
            input.setText(text)
            val position = input.length()
            val etext = input.text
            Selection.setSelection(etext, position)

            val noticeView = dialogView.findViewById<TextView>(R.id.noticeText)
            noticeView.text = notice

            // Set up the buttons
            builder.setPositiveButton(
                "OK"
            ) { _, _ -> lambdaYes(input.text.toString()) }
            builder.setNegativeButton(
                "Cancel"
            ) { _, _ -> lambdaNo() }

            builder.show()
        }

        fun getCalendarPicker(context : Context, lambdaOk : (Int, Int, Int) -> Unit)
        {
            val myCalendar = Calendar.getInstance()

            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // TODO Auto-generated method stub
                lambdaOk(year, monthOfYear, dayOfMonth)
            }

            DatePickerDialog(context, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}