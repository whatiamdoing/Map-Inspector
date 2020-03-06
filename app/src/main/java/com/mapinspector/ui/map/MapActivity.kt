package com.mapinspector.ui.map

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.mapinspector.R
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.ui.map.fragments.MapListFragment
import com.mapinspector.utils.Constants.Delay.MAP_READY_DELAY
import com.mapinspector.utils.Constants.Delay.SPLASH_TIME_DELAY
import com.mapinspector.utils.setVisible
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    private val mapTab by lazy { MapFragment() }
    private val listTab by lazy { MapListFragment() }
    private var current: Fragment? = null

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
                    bottom_navigation.setVisible()
            }, MAP_READY_DELAY)
        }, SPLASH_TIME_DELAY)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_map -> {
                current = mapTab
                    supportFragmentManager.beginTransaction().run {
                    replace(
                        R.id.fragment_map,
                        mapTab,
                        MapListFragment().javaClass.simpleName
                    )
                        commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.nav_list -> {
                current = listTab
                supportFragmentManager.beginTransaction().run{
                    replace(
                        R.id.fragment_map,
                        listTab,
                        MapFragment().javaClass.simpleName
                    )
                        commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }

    fun showMessage(messageText: String) {
        findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(it, messageText, Snackbar.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, messageText, Toast.LENGTH_SHORT).show()
    }
}
