package com.mapinspector.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mapinspector.db.enity.PlaceDTO

@Database(entities = [PlaceDTO::class], version = 1)
@TypeConverters(Converters::class)
abstract class PlaceDatabase : RoomDatabase() {

    abstract fun getPlaceDAO(): PlaceDAO

}