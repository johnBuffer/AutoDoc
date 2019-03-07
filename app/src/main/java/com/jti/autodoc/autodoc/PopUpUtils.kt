package com.jti.autodoc.autodoc

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class PopUpUtils
{
    companion object {
        fun getConfirmationPopUp(question : String, lambdaYes: () -> Unit, lambdaNo: () -> Unit, context : Context) : AlertDialog.Builder
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
    }
}