package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils

import com.mapinspector.R
import com.mapinspector.utils.Constants.Delay.SPLASH_TIME_DELAY
import kotlinx.android.synthetic.main.fragment_splash.*

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val fadeInAnimation = AnimationUtils.loadAnimation(activity!!, R.anim.fade_in)
        iv_logo.startAnimation(fadeInAnimation)
    }
}
