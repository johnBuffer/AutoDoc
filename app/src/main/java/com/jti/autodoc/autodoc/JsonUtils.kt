package com.jti.autodoc.autodoc

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileOutputStream

class JsonUtils
{
    companion object {
        fun loadPendingIDs(array : JSONArray) : ArrayList<MedocDataTime>
        {
            val result = ArrayList<MedocDataTime>()
            for (i in 0 until array.length())
            {
                val jsonData : JSONObject = array.getJSONObject(i)
                result.add(MedocDataTime(jsonData))
            }

            return result
        }

        fun loadTracks(array : JSONArray) : ArrayList<Track>
        {
            val result = ArrayList<Track>()
            for (i in 0 until array.length())
            {
                val currentTrack = array.getJSONObject(i)
                val newTrack = Track()
                newTrack.fromJson(currentTrack)
                result.add(newTrack)
            }

            return result
        }

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

        fun pendingsArrayToJson(pendings : ArrayList<MedocDataTime>) : JSONObject
        {
            val root = JSONObject()
            val pendingArray = JSONArray()
            for (medoc : MedocDataTime in pendings)
            {
                pendingArray.put(medoc.toJson())
            }
            root.put("pendingIds", pendingArray)

            return root
        }

        fun tracksArrayToJson(tracks : ArrayList<Track>) : JSONObject
        {
            val root = JSONObject()
            val tracksArray = JSONArray()
            for (track : Track in tracks)
            {
                tracksArray.put(track.toJson())
            }
            root.put("tracks", tracksArray)

            return root
        }
    }
}