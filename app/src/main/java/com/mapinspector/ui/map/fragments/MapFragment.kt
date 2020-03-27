package com.mapinspector.ui.map.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.faltenreich.skeletonlayout.Skeleton
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mapinspector.R
import com.mapinspector.di.App
import com.mapinspector.db.enity.PlaceDTO
import com.mapinspector.utils.Constants.Others.ZOOM_TO_CURRENT_LOCATION
import com.mapinspector.utils.Constants.Others.ZOOM_TO_SELECT_PLACE
import com.mapinspector.utils.adapter.infoWindow.CustomInfoWindowAdapter
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.viewmodel.MapListViewModel
import kotlinx.android.synthetic.main.fragment_map.*
import javax.inject.Inject

private const val REQUEST_PERMISSIONS = 10
class MapFragment : Fragment(), GoogleMap.OnMarkerClickListener, BottomDialogFragment.OnDismissListener, BottomDialogFragment.OnCompleteListener {

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var googleMap: GoogleMap
    private lateinit var locationManager: LocationManager
    @Inject
    lateinit var mapViewModel: MapListViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    lateinit var marker: Marker
    lateinit var selectMarker: Marker
    val markers: MutableList<Marker> = ArrayList()
   private lateinit var skeleton: Skeleton

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
        skeleton = skeleton_layout
        skeleton.showSkeleton()
        initMap()
        App.appComponent.inject(this)
        mapViewModel = ViewModelProviders.of(this).get(MapListViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
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
            addExistingMarkets()
        }
    }

    private fun showBottomDialog(latLng: LatLng) {
        marker =  googleMap.addMarker(MarkerOptions().position(latLng))
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
                    skeleton.showOriginal()
                    val location: Location? = task.result
                    if (location != null) {
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), ZOOM_TO_CURRENT_LOCATION)
                        )
                    }
                }
            }
        }
    }

     fun addExistingMarkets() {
        googleMap.clear()
         sharedPref.getUserId()?.let{
             mapViewModel.loadPlaces(it)
         }
        mapViewModel.places.observe(this, Observer { it ->
            it.forEach {
                marker = googleMap.addMarker(
                    MarkerOptions().apply{
                        position(
                            LatLng(
                                it.placeCoordinates.lat,
                                it.placeCoordinates.lng
                            )
                        )
                        title(it.placeName)
                    }
                )
                markers += marker
            }
            googleMap.setInfoWindowAdapter(
                CustomInfoWindowAdapter(activity!!)
            )
        })
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        return true
    }

    override fun onDialogDismissed() {
        marker.remove()
    }

    override fun onComplete(placeName: String) {
        marker.title = placeName
    }

    fun showPlace(place: PlaceDTO){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(
                place.placeCoordinates.lat,
                place.placeCoordinates.lng
            ),
            ZOOM_TO_SELECT_PLACE)
        )
        selectMarker =  googleMap.addMarker(MarkerOptions().position(
            LatLng(
                place.placeCoordinates.lat,
                place.placeCoordinates.lng
            )
        ))
        selectMarker.title = place.placeName
        selectMarker.showInfoWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        selectMarker.hideInfoWindow()
        selectMarker.remove()
    }
}


