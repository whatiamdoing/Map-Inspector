package com.mapinspector.utils.adapter.infoWindow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mapinspector.R
import com.mapinspector.enity.Coordinates
import com.mapinspector.enity.PlaceDTO
import kotlinx.android.synthetic.main.marker_info_window.view.*

class CustomInfoWindowAdapter(val context: Context): GoogleMap.InfoWindowAdapter{

    override fun getInfoContents(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.marker_info_window, null)
        view.tv_infoWindow_coordinates.text = view.tv_infoWindow_coordinates.resources.getString(
            R.string.latLng,
            marker!!.position.latitude.toString(),
            marker.position.longitude.toString()
        )
        view.tv_infoWindow_placeName.text = marker.title

        return view
    }

    override fun getInfoWindow(marker: Marker?) = null
}