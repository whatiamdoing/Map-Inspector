package com.mapinspector.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.mapinspector.db.enity.PlaceDTO
import io.reactivex.Single

@Dao
@TypeConverters(Converters::class)
interface PlaceDAO {

    @Insert
    fun insertAll(places: List<PlaceDTO>)

    @Query("SELECT * FROM placeDTO")
    fun getAllPlaces(): Single<List<PlaceDTO>>

}