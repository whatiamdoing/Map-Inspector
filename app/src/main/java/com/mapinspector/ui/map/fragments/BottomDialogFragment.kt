package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.di.App
import com.mapinspector.R
import com.mapinspector.model.Coordinates
import com.mapinspector.model.LocationModel
import com.mapinspector.utils.setGone
import com.mapinspector.utils.setVisible
import com.mapinspector.viewmodel.BottomDialogViewModel
import kotlinx.android.synthetic.main.fragment_dialog.*
import java.util.*
import javax.inject.Inject

class BottomDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var dialogViewModel: BottomDialogViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    private val coordinates by lazy { arguments!!.get("coordinates") as LocationModel }

    companion object {
        fun newInstance(coordinates: LatLng) = BottomDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable("coordinates", LocationModel(coordinates))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        App.appComponent.inject(this)
        initUser()
        dialogViewModel = ViewModelProviders.of(this).get(BottomDialogViewModel::class.java)
        tv_object_cord.text = getString(
            R.string.latLng,
            coordinates.latLng.latitude.toString(),
            coordinates.latLng.longitude.toString()
        )
        setOnClickListeners()
        observeSuccessMessage()
        setLoadingObserver()
    }

    private fun setOnClickListeners() {
        btn_apply.setOnClickListener {
            val placeName = ed_enter_name.text.toString().trimIndent()
            val placeId = UUID.randomUUID().toString()
            dialogViewModel.createPlace(
                sharedPref.getUserId()!!,
                placeName,
                Coordinates(coordinates.latLng.latitude, coordinates.latLng.longitude),
                placeId
            )
        }
        btn_back.setOnClickListener {
            dismiss()
        }
    }

    private fun initUser() {
        if (sharedPref.getIsFirstLaunch()) {
            sharedPref.setUserId(UUID.randomUUID().toString())
            sharedPref.setFirstLaunch(false)
        }
    }

    private fun observeSuccessMessage() {
        dialogViewModel.errorLiveData.observe(this, androidx.lifecycle.Observer {
            (Toast.makeText(activity!!, "Ошибка!", Toast.LENGTH_SHORT).show())
        })
    }

    private fun setLoadingObserver(){
       dialogViewModel.isLoading.observe(this, androidx.lifecycle.Observer {
            it?.let{
                if (it){
                    pb_dialog.setVisible()
                } else {
                    pb_dialog.setGone()
                    dismiss()
                }
            }
       })
    }
}
