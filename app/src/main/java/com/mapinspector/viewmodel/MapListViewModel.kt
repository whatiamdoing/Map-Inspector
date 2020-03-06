package com.mapinspector.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapinspector.di.App
import com.mapinspector.di.network.ApiService
import com.mapinspector.enity.Place
import com.mapinspector.enity.PlaceDTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapListViewModel: ViewModel() {

    @Inject lateinit var apiService: ApiService

    init{
        App.appComponent.inject(this)
    }

    private val subscriptions = CompositeDisposable()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveData = MutableLiveData<Throwable>()
    var places: MutableLiveData<List<PlaceDTO>> = MutableLiveData()

    fun loadPlaces(userId: String){
        subscriptions.add(
            apiService.getPlaces(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {isLoading.value = true }
                .doOnTerminate {isLoading.value = false}
                .subscribe(
                    { onRetrievePlaceListSuccess(it) },
                    { errorLiveData.value = it }
                )
        )
    }

    private fun onRetrievePlaceListSuccess(it: Map<String, Place>): List<PlaceDTO> {
        val placeList = it.map {
            PlaceDTO(
                it.key,
                it.value.placeName,
                it.value.placeCoordinates
            )
        }.sortedBy { it.placeName }
        places.value = placeList
        return placeList
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }
}