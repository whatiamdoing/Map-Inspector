package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapinspector.di.App
import com.mapinspector.R
import com.mapinspector.ui.map.viewmodels.BottomDialogViewModel
import com.mapinspector.utils.getPlaceNumber
import com.mapinspector.utils.getUserId
import com.mapinspector.utils.setPlaceNumber
import kotlinx.android.synthetic.main.fragment_dialog.*
import javax.inject.Inject


class BottomDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var dialogViewModel: BottomDialogViewModel

    companion object {
        fun newInstance(coordinates: String) = BottomDialogFragment().apply {
            arguments = Bundle().apply {
                putString("coordinates", coordinates)
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
        dialogViewModel = ViewModelProviders.of(this).get(BottomDialogViewModel::class.java)
        val coordinates by lazy { arguments?.getString("coordinates") }
        tv_object_cord.text = getString(R.string.latLng, coordinates!!.substringAfter(":"))
        setOnClickListeners(coordinates!!.substringAfter(" "))
    }

    private fun setOnClickListeners(coordinates: String?) {
        btn_apply.setOnClickListener {
            val placeName = ed_enter_name.text.toString()
            val placeNumber = getPlaceNumber(activity!!)
            dialogViewModel.createPlace(getUserId(activity!!)!!, placeName, coordinates!!, placeNumber)
            setPlaceNumber(activity!!, placeNumber + 1)
            dismiss()
        }
        btn_back.setOnClickListener {
            dismiss()
        }
    }
}
