package com.jti.autodoc.autodoc

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import java.util.*
import android.text.InputType
import android.widget.EditText
import android.app.DatePickerDialog
import android.view.View
import android.widget.DatePicker





class PopUpUtils
{
    companion object {
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

        fun getTextDialog(context : Context, title : String, lambdaYes: (String) -> Unit, lambdaNo: () -> Unit)
        {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)

            // Set up the input
            val input = EditText(context)
            //input.setPadding(16, 0, 16, 0)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

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