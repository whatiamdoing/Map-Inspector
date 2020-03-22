package com.mapinspector.enity

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    val latLng: LatLng
) : Parcelable