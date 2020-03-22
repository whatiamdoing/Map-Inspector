package com.mapinspector.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mapinspector.ui.map.MapActivity

abstract class BaseBDF: BottomSheetDialogFragment() {
    fun showMessage(message: String) {
        (activity!! as MapActivity).showMessage(message)
    }
}