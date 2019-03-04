package com.jti.autodoc.autodoc

data class DayDataModel(val dayID: Int)
{
    val medocs: MutableList<MedocData> = ArrayList()

    fun addMedoc(name: String, time: Int)
    {
        medocs.add(MedocData(name, time))
    }
}