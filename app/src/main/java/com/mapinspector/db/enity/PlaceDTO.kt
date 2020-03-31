package com.mapinspector.db.enity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaceDTO(
    @PrimaryKey
    val placeId: String,
    val placeName: String,
    val placeCoordinates: Coordinates
)