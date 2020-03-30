package com.mapinspector.di.module

import android.content.Context
import androidx.room.Room
import com.mapinspector.db.PlaceDAO
import com.mapinspector.db.PlaceDatabase
import dagger.Module
import dagger.Provides

@Module
object RoomModule {

    private const val DB_NAME = "place.db"

    @Volatile
    private var instance: PlaceDatabase? = null

    @Provides
    fun providesPlaceDao(placeDatabase: PlaceDatabase): PlaceDAO {
        return placeDatabase.getPlaceDAO()
    }

    @Provides
    fun getInstance(context: Context): PlaceDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it}
        }
    }

    private fun buildDatabase(context: Context): PlaceDatabase {
        return Room
            .databaseBuilder(context.applicationContext, PlaceDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}