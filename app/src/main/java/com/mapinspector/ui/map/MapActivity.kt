package com.mapinspector.ui.map

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.ui.map.fragments.MapListFragment
import com.mapinspector.utils.Constants.Delay.MAP_READY_DELAY
import com.mapinspector.utils.Constants.Delay.SPLASH_TIME_DELAY
import com.mapinspector.utils.setGone
import com.mapinspector.utils.setVisible
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        bottom_navigation.setOnNavigationItemSelectedListener(navListener)

        Handler().postDelayed({
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_map, MapFragment())
                addToBackStack(null)
                commit()
            }
            Handler().postDelayed({
                if(isMapReady()){
                    bottom_navigation.setVisible()
                } else {
                    bottom_navigation.setGone()
                }
            }, MAP_READY_DELAY)
        }, SPLASH_TIME_DELAY)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_map -> {
                supportFragmentManager.beginTransaction().run {
                    replace(
                        R.id.fragment_map,
                        MapFragment(),
                        MapFragment().javaClass.simpleName
                    )
                        commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_list -> {
                supportFragmentManager.beginTransaction().run{
                    replace(
                        R.id.fragment_map,
                        MapListFragment(),
                        MapFragment().javaClass.simpleName
                    )
                        commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }

    fun isMapReady():Boolean {
        return true
    }

}
