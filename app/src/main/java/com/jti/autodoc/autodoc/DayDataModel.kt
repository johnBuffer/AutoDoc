package com.jti.autodoc.autodoc

import org.json.JSONArray
import org.json.JSONObject

data class DayDataModel(val dayID: Int)
{
    val medocs: ArrayList<MedocData> = ArrayList()

    constructor(jsonData : JSONObject) : this(jsonData.getInt("id"))
    {
        var medocsArray = jsonData.getJSONArray("medocs")
        for (j in 0 until medocsArray.length())
        {
            var medocJson = medocsArray.getJSONObject(j)
            addMedoc(MedocData(medocJson))
        }
    }

    fun addMedoc(medoc : MedocData)
    {
        medocs.add(medoc)
    }

    fun addMedoc(name: String, time: String)
    {
        medocs.add(MedocData(name, time))
    }

    fun getLastMedoc() : MedocData
    {
        return medocs.last()
    }

    fun toJson() : JSONObject
    {
        var dayData = JSONObject()
        var medocArray = JSONArray()

        for (medoc : MedocData in medocs)
        {
            medocArray.put(medoc.toJson())
        }

        dayData.put("id", dayID)
        dayData.put("medocs", medocArray)

        return dayData
    }

    fun sortMedocs()
    {
        medocs.sortBy { medoc : MedocData -> medoc.time }
    }
}