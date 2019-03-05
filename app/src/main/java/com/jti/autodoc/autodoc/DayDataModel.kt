package com.jti.autodoc.autodoc

data class DayDataModel(val dayID: Int)
{
    val medocs: ArrayList<MedocData> = ArrayList()

    fun addMedoc(name: String, time: String)
    {
        medocs.add(MedocData(name, time))
    }

    fun getLastMedoc() : MedocData
    {
        return medocs.last()
    }
}