package com.mapinspector.ui.map.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.BottomDialogFragment.Companion.newInstance

private const val PERMISSION_REQUEST = 10

class MapFragment : Fragment() {

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

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
        initMap()
    }

    private fun initMap(){
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            setOnMapClickListeners()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkPermission(activity!!, permissions)) {
                    googleMap.isMyLocationEnabled = true
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

    private fun setOnMapClickListeners(){
        googleMap.setOnMapClickListener {
            googleMap.addMarker(MarkerOptions().position(it))
        }
        googleMap.setOnMapLongClickListener {
            showBottomDialog(it)
        }
    }

    private fun showAlert(){
       AlertDialog.Builder(activity).apply {
            setTitle("Нужно разрешение")
            setMessage("Разрешение необходимо для выполнения задачи.")
            setPositiveButton("OK") { dialog, which ->   requestPermissions(permissions, PERMISSION_REQUEST)}
            setNegativeButton("Cancel") { dialog, which -> showAlert() }
        }.apply {
            create()
            show()
        }
    }

    private fun checkPermission(context: Context, permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices){
            if(checkCallingOrSelfPermission(context, permissionArray[i]) == PermissionChecker.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST){
            var allSuccess = true
            for(i in permissions.indices){
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allSuccess = false
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])){
                        Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show()
                        requestPermissions(permissions, PERMISSION_REQUEST)
                    }else {

                        Toast.makeText(context,"Go to settings and enable the permission",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if(allSuccess){
                googleMap.isMyLocationEnabled = true
                Toast.makeText(context,"Permissions Granted",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
