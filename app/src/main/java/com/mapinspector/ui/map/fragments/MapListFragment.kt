package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapinspector.R
import com.mapinspector.base.BaseFragment
import com.mapinspector.di.App
import com.mapinspector.ui.map.MapActivity
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.utils.adapter.recycler.Adapter
import com.mapinspector.viewmodel.MapListViewModel
import kotlinx.android.synthetic.main.fragment_map_list.*
import javax.inject.Inject

class MapListFragment : BaseFragment() {

    @Inject
    lateinit var mapListViewModel: MapListViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: Adapter

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
        observeUnSuccessMessage()
        setPlaceListObserver()
        initRecycler()
    }

    override fun onResume() {
        mapListViewModel.loadPlaces(sharedPref.getUserId()!!)
        super.onResume()
    }
    private fun setLoadingObserver(){
        mapListViewModel.isLoading.observe(this, Observer {
            it?.let{
                pb_list.isVisible = it
            }
        })
    }

    private fun observeUnSuccessMessage(){
        mapListViewModel.errorLiveData.observe(this, androidx.lifecycle.Observer{
            showMessage(getString(R.string.error))
        })
    }

    private fun setPlaceListObserver() {
        mapListViewModel.places.observe(this, Observer {
            it?.let {
                adapter.updateList(it)
            }
        })
    }

    private fun initRecycler(){
        adapter = Adapter(arrayListOf(),
            {
            mapListViewModel.deletePlace(sharedPref.getUserId()!!,it.placeId)
        },
            {
                (activity!! as MapActivity).selectMarker(it)
        })
        place_list?.adapter = adapter
        place_list?.layoutManager = LinearLayoutManager(activity!!)
    }
}
