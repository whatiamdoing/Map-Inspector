package com.mapinspector.ui.map.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.di.App
import com.mapinspector.R
import com.mapinspector.base.BaseBottomSheetDialogFragment
import com.mapinspector.enity.Coordinates
import com.mapinspector.enity.LocationModel
import com.mapinspector.utils.setGone
import com.mapinspector.utils.setVisible
import com.mapinspector.viewmodel.BottomDialogViewModel
import kotlinx.android.synthetic.main.fragment_dialog.*
import java.util.*
import javax.inject.Inject

class BottomDialogFragment : BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var dialogViewModel: BottomDialogViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    private val coordinates by lazy { arguments!!.get("coordinates") as LocationModel }
    var isMarkerCanceled = true

    companion object {
        fun newInstance(coordinates: LatLng) = BottomDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable("coordinates", LocationModel(coordinates))
            }
        }
    }

    private var listener: OnDismissListener? = null
    private var placeListener: OnCompleteListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as? OnDismissListener
        placeListener = parentFragment as? OnCompleteListener
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
            isMarkerCanceled = false
            val placeName = ed_enter_name.text.toString().trimIndent()
            val placeId = UUID.randomUUID().toString()
            dialogViewModel.createPlace(
                sharedPref.getUserId()!!,
                placeName,
                Coordinates(coordinates.latLng.latitude, coordinates.latLng.longitude),
                placeId
            )
            placeListener!!.onComplete(placeName)
        }
        btn_back.setOnClickListener {
            isMarkerCanceled = true
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if(isMarkerCanceled) {
            listener?.onDialogDismissed()
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
            showMessage(getString(R.string.error))
        })
    }

    private fun setLoadingObserver() {
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

    interface OnDismissListener {
        fun onDialogDismissed()
    }

    interface OnCompleteListener {
        fun onComplete(placeName: String)
    }
}
