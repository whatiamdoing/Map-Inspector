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
import com.mapinspector.db.enity.PlaceDTO
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.ui.map.fragments.MapListFragment
import com.mapinspector.utils.Constants.Delay.MAP_READY_DELAY
import com.mapinspector.utils.Constants.Delay.SPLASH_TIME_DELAY
import com.mapinspector.utils.setVisible
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    companion object {
        const val TAG_MAP = "map_fragment"
        const val TAG_LIST = "list_fragment"
    }

    private var currentTab: TABS = TABS.MAP

    enum class TABS(val fragment: Fragment, val tag: String) {
        MAP(MapFragment(), TAG_MAP),
        LIST(MapListFragment(), TAG_LIST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        bottom_navigation.setOnNavigationItemSelectedListener(navListener)

        Handler().postDelayed({
            bottom_navigation.selectedItemId = R.id.nav_map
            Handler().postDelayed({
                    bottom_navigation.setVisible()
            }, MAP_READY_DELAY)
        }, SPLASH_TIME_DELAY)
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.nav_map -> {
                selectTab(TABS.MAP)
            }
            R.id.nav_list -> {
                selectTab(TABS.LIST)
                supportFragmentManager.beginTransaction().detach(TABS.LIST.fragment).commitNow()
                supportFragmentManager.beginTransaction().attach(TABS.LIST.fragment).commitNow()
            }
        }
        true
    }

    private fun selectTab(tab: TABS) {
        val currentFragment = supportFragmentManager
            .findFragmentByTag(currentTab.tag)

        val newFragment = supportFragmentManager
            .findFragmentByTag(tab.tag)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        supportFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                R.id.fragment_map,
                tab.fragment,
                tab.tag
            )
            currentFragment?.let {
                hide(it)
            }
            newFragment?.let {
                show(it)
            }
        }.commitNow()
        currentTab = tab
    }

    fun showMessage(messageText: String) {
        findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(it, messageText, Snackbar.LENGTH_SHORT).show()
        } ?: Toast.makeText(this, messageText, Toast.LENGTH_SHORT).show()
    }

    fun selectMarker(place: PlaceDTO) {
        selectTab(TABS.MAP)
        bottom_navigation.selectedItemId = R.id.nav_map
        (supportFragmentManager.findFragmentByTag(TAG_MAP) as MapFragment).showPlace(place)
    }

    fun removeMarker(place: PlaceDTO){
        val markers = (supportFragmentManager.findFragmentByTag(TAG_MAP) as MapFragment).markers
        markers.forEach {
            when(it.title){
                place.placeName -> it.remove()
            }
        }
        (supportFragmentManager.findFragmentByTag(TAG_MAP) as MapFragment).addExistingMarkets()
    }
}
