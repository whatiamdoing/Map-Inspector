package com.mapinspector.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.utils.Constants

class MapActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_map)

            Handler().postDelayed({
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_map, MapFragment())
                    addToBackStack(null)
                    commit()
                }
            }, Constants.Delay.SPLASH_TIME_DELAY)
        }
}
