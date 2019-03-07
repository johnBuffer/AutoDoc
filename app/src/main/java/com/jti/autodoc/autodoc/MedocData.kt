package com.jti.autodoc.autodoc

import org.json.JSONObject

data class MedocData(var description: String, var time: String)
{
    constructor(jsonData : JSONObject) : this(jsonData.getString("description"), jsonData.getString("time"))

    fun toJson() : JSONObject
    {
        var medocData = JSONObject()
        medocData.put("description", description)
        medocData.put("time", time)

        return medocData
    }
}