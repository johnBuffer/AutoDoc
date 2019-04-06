package com.jti.autodoc.autodoc;

import android.content.Context
import java.io.BufferedReader
import java.io.FileNotFoundException
import kotlin.jvm.Throws;

import java.io.InputStream;
import java.io.InputStreamReader

public class FileUtils {

    companion object {
        @Throws(Exception::class)
        fun convertStreamToString(istr: InputStream): String {
            val reader = BufferedReader(InputStreamReader(istr))
            val sb = StringBuilder()
            var line: String? = reader.readLine()
            while (line != null) {
                sb.append(line).append("\n")
                line = reader.readLine()
            }
            reader.close()
            return sb.toString()
        }

        @Throws(Exception::class)
        fun getStringFromFile(filePath: String, context: Context): String {
            try {
                val fl = context.openFileInput(filePath)
                val ret = convertStreamToString(fl)
                //Make sure you close all streams.
                fl.close()
                return ret
            }
            catch (_ : FileNotFoundException) {
            }

            return ""
        }
    }
}
