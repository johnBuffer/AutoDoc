package com.jti.autodoc.autodoc

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileOutputStream

class JsonUtils
{
    companion object {
        fun writeJsonToFile(filename: String, data : JSONObject, context : Context)
        {
            val outputStream: FileOutputStream
            try {
                outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

                outputStream.write(data.toString().toByteArray())
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}