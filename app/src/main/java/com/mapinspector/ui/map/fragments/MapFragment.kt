package com.mapinspector.ui.map.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
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
import com.mapinspector.ui.map.MapActivity
import com.mapinspector.utils.Constants.Delay.FASTEST_INTERVAL
import com.mapinspector.utils.Constants.Delay.UPGRADE_INTERVAL
import com.mapinspector.utils.Constants.Quantity.NUMBER_OF_UPGRADES

private const val REQUEST_PERMISSIONS = 10

class MapFragment : Fragment() {

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap: GoogleMap
    private val allPoints = mutableListOf<LatLng>()
    private lateinit var locationManager: LocationManager

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

    override fun onResume() {
            zoomToMyLocation()
            super.onResume()
    }

    private fun initMap() {
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            setOnMapClickListeners()
                if (isPermissionsGranted()) {
                    googleMap.isMyLocationEnabled = true
                } else {
                    googleMap.isMyLocationEnabled = false
                    showPermissionAlert()
                }
            (activity!! as MapActivity).isMapReady()
        }
    }

    private fun showBottomDialog(latLng: LatLng) {
        allPoints.add(latLng)
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng))
        BottomDialogFragment.newInstance(latLng).show(childFragmentManager, null)
    }

    private fun setOnMapClickListeners() {
        googleMap.setOnMapLongClickListener {
            showBottomDialog(it)
        }
    }

    private fun showPermissionAlert() {
        AlertDialog.Builder(activity).run{
            setCancelable(false)
            setTitle(getString(R.string.need_permission))
            setMessage(getString(R.string.permission_need_for))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                requestPermissions(
                    permissions,
                    REQUEST_PERMISSIONS
                )
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> showPermissionAlert() }
            create()
            show()
        }
    }

    private fun showGpsAlert() {
        AlertDialog.Builder(activity).run{
            setCancelable(false)
            setTitle(getString(R.string.enable_geoData))
            setMessage(getString(R.string.pls_enable_geoData))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            setNegativeButton(getString(R.string.no)) { _, _ -> showGpsAlert() }
            create()
            show()
        }
    }

    private fun isPermissionsGranted(): Boolean {
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
        if (requestCode == REQUEST_PERMISSIONS) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleMap.isMyLocationEnabled = true
                } else {
                    requestPermissions(permissions, REQUEST_PERMISSIONS)
                }
            }
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private  fun zoomToMyLocation() {
        if (isPermissionsGranted()) {
            if (!isLocationEnabled()) {
                showGpsAlert()
            } else {
                mFusedLocationClient.lastLocation.addOnCompleteListener(activity!!) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude), 13f))
                    }
                }
            }
        }
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = UPGRADE_INTERVAL
            fastestInterval = FASTEST_INTERVAL
            numUpdates = NUMBER_OF_UPGRADES
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
