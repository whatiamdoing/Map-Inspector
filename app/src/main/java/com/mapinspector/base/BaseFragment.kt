package com.mapinspector.base

import androidx.fragment.app.Fragment
import com.mapinspector.ui.map.MapActivity

abstract class BaseFragment: Fragment() {
    fun showMessage(message: String) {
        (activity!! as MapActivity).showMessage(message)
    }
}