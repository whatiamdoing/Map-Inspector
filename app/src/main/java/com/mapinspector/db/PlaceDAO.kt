package com.mapinspector.db

import androidx.room.*
import com.mapinspector.db.enity.PlaceDTO
import io.reactivex.Observable

@Dao
@TypeConverters(Converters::class)
interface PlaceDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(places: List<PlaceDTO>)

    @Query("SELECT * from PlaceDTO")
    fun getAllPlaces(): Observable<List<PlaceDTO>>

}