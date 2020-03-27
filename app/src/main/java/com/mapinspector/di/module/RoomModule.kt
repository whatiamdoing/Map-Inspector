package com.mapinspector.di.module

import android.app.Application
import androidx.room.Room
import com.mapinspector.db.PlaceDAO
import com.mapinspector.db.PlaceDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: Application): PlaceDatabase {
        return Room.databaseBuilder(application, PlaceDatabase::class.java, "place-database")
            .build()
    }

    @Singleton
    @Provides
    fun providesProductDao(placeDatabase: PlaceDatabase): PlaceDAO {
        return placeDatabase.getPlaceDAO()
    }

}