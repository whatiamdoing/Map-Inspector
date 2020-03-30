package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapinspector.R
import com.mapinspector.di.App
import com.mapinspector.ui.map.MapActivity
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.utils.adapter.recycler.Adapter
import com.mapinspector.viewmodel.MapListViewModel
import kotlinx.android.synthetic.main.fragment_map_list.*
import javax.inject.Inject

class MapListFragment : Fragment() {

    @Inject
    lateinit var mapListViewModel: MapListViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: Adapter
    private var isFirstTimeOpened = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map_list, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        App.appComponent.inject(this)
        mapListViewModel = ViewModelProviders.of(this).get(MapListViewModel::class.java)
        setLoadingObserver()
        setPlaceListObserver()
        observeUnSuccessMessage()
        initRecycler()
        getPlacesFromDaoOrApi()
    }

    private fun getPlacesFromDaoOrApi() {
        if(isFirstTimeOpened) {
            sharedPref.getUserId()?.let {
                mapListViewModel.getPlacesFromApi(it)
            }
            isFirstTimeOpened = false
        } else {
            mapListViewModel.getPlaces()
        }
    }

    private fun setLoadingObserver() {
        mapListViewModel.isLoading.observe(this, Observer {
            it?.let {
                pb_list.isVisible = it
            }
        })
    }

    private fun setPlaceListObserver() {
        mapListViewModel.places.observe(this, Observer {
            it?.let {
                adapter.updateList(it)
            }
        })
    }

    private fun observeUnSuccessMessage(){
        mapListViewModel.errorLiveEvent.observe(this, Observer {
            (activity!! as MapActivity).showMessage(getString(R.string.error))
        })
    }

    private fun initRecycler(){
        adapter = Adapter(arrayListOf(),
            {
                mapListViewModel.deletePlace(sharedPref.getUserId()!!,it.placeId)
                (activity!! as MapActivity).removeMarker(it)
            },
            {
                (activity!! as MapActivity).selectMarker(it)
            }
        )
        place_list?.adapter = adapter
        place_list?.layoutManager = LinearLayoutManager(activity!!)
    }
}
