package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapinspector.R
import kotlinx.android.synthetic.main.fragment_dialog.*

class BottomDialogFragment : BottomSheetDialogFragment() {

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
        val text by lazy { arguments?.getString("coordinates") }
        tv_object_cord.text = text
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        btn_apply.setOnClickListener {
        }
    }
}