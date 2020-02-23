package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.BottomDialogFragment.Companion.newInstance
import com.mapinspector.utils.getIsFirstLaunch
import com.mapinspector.utils.setFirstLaunch
import com.mapinspector.utils.setUserId
import java.util.*


class MapFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private val allPoints = mutableListOf<LatLng>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        initUser()
        initMap()
    }

    private fun showBottomDialog(it: LatLng) {
        allPoints.add(it)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(it))
        newInstance(it.toString()).show(childFragmentManager, null)
    }

    private fun initMap(){
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.isMyLocationEnabled = true
            setOnMapClickListeners()
        }
    }

    private fun setOnMapClickListeners(){
        googleMap.setOnMapClickListener {
            googleMap.addMarker(MarkerOptions().position(it))
        }
        googleMap.setOnMapLongClickListener {
            showBottomDialog(it)
        }
    }

    private fun initUser(){
        if (getIsFirstLaunch(activity!!)){
            val userId = UUID.randomUUID().toString()
            setUserId(activity!!, userId)
            setFirstLaunch(activity!!, false)
        }
    }
}
