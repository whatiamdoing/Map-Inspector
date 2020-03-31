package com.mapinspector.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.mapinspector.db.enity.Coordinates
import java.lang.Exception

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun coordinatesToJson(placeCoordinates: Coordinates): String = gson.toJson(placeCoordinates)

    @TypeConverter
    fun coordinatesFromJson(jsonData: String?) =
        try {
            gson.fromJson(jsonData, Coordinates::class.java)
        } catch (e: Exception) {
            null
        }

}