package com.mapinspector.ui.map.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapinspector.R
import com.mapinspector.base.BaseFragment
import com.mapinspector.di.App
import com.mapinspector.enity.PlaceDTO
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.utils.adapter.recycler.Adapter
import com.mapinspector.utils.setGone
import com.mapinspector.utils.setVisible
import com.mapinspector.viewmodel.MapListViewModel
import kotlinx.android.synthetic.main.fragment_map_list.*
import javax.inject.Inject


class MapListFragment : BaseFragment() {

    @Inject
    lateinit var mapListViewModel: MapListViewModel
    @Inject
    lateinit var sharedPref: SharedPreferences
    private lateinit var adapter: Adapter
    private lateinit var list: MutableList<PlaceDTO>

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
        initRecycler()
        setPlaceListObserver()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        adapter.swap(list, position)
        
    }

    override fun onResume() {
        mapListViewModel.loadPlaces(sharedPref.getUserId()!!)
        super.onResume()
    }
    private fun setLoadingObserver(){
        mapListViewModel.isLoading.observe(this, Observer {
            it?.let{
                if (it){
                    pb_list.setVisible()
                } else {
                    pb_list.setGone()
                }
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
            list = it.toMutableList()
            it?.let {
                adapter.updateList(it.toMutableList())
            }
        })
    }

    private fun initRecycler(){
        adapter = Adapter(arrayListOf())
        place_list?.adapter = adapter
        place_list?.layoutManager = LinearLayoutManager(activity!!)
        adapter.setOnItemClickListener(object: Adapter.OnItemClickListener{
            override fun onDeleteClick(position: Int) {
                removeItem(position)
            }
        })
    }
}
