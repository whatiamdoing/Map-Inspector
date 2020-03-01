package com.mapinspector.ui.map.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.BottomDialogFragment.Companion.newInstance

private const val PERMISSION_REQUEST = 10

class MapFragment : Fragment() {

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var mapFragment: SupportMapFragment
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap: GoogleMap
    private val allPoints = mutableListOf<LatLng>()
    lateinit var locationManager: LocationManager

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        initMap()
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            setOnMapClickListeners()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermissions()) {
                    googleMap.isMyLocationEnabled = true
                    getLastLocation()
                } else {
                    showAlert()
                }
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }
    }

    private fun showBottomDialog(it: LatLng) {
        allPoints.add(it)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(it))
        newInstance(it).show(childFragmentManager, null)
    }

    private fun setOnMapClickListeners() {
        googleMap.setOnMapClickListener {
            googleMap.addMarker(MarkerOptions().position(it))
        }
        googleMap.setOnMapLongClickListener {
            showBottomDialog(it)
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(activity).apply {
            setCancelable(false)
            setTitle(getString(R.string.need_permission))
            setMessage(getString(R.string.permission_need_for))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                requestPermissions(
                    permissions,
                    PERMISSION_REQUEST
                )
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> showAlert() }
        }.apply {
            create()
            show()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true

        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap.isMyLocationEnabled = true
                    if (!isLocationEnabled()){
                        gpsAlert()
                    } else {
                        getLastLocation()
                    }
                } else {
                    requestPermissions(permissions, PERMISSION_REQUEST)
                }
            }
    }
    private fun isLocationEnabled(): Boolean {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun gpsAlert(){
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.apply {
            setCancelable(false)
            setTitle(getString(R.string.enable_geoData))
            setMessage(getString(R.string.pls_enable_geoData))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> gpsAlert() }
        }.apply {
            create()
            show()
        }
    }

    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude), 13f))
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST)
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
            numUpdates = 1
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastLocation.latitude,mLastLocation.longitude), 13f))
        }
    }
}
