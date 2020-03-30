package com.mapinspector.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapinspector.db.PlaceDAO
import com.mapinspector.di.App
import com.mapinspector.di.network.ApiService
import com.mapinspector.db.enity.PlaceDTO
import com.mapinspector.utils.SharedPreferences
import com.mapinspector.utils.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapListViewModel: ViewModel() {

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var placeDAO: PlaceDAO
    @Inject
    lateinit var sharedPref: SharedPreferences

    init {
        App.appComponent.inject(this)
    }

    private val loadSubscriptions = CompositeDisposable()
    private val deleteSubscriptions = CompositeDisposable()
    private val roomSubscriptions = CompositeDisposable()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorLiveEvent: MutableLiveData<Throwable> = MutableLiveData()
    var places: SingleLiveEvent<List<PlaceDTO>> = SingleLiveEvent()

    fun getPlaces() {
        roomSubscriptions.add(
            Observable.concat(
                getPlacesFromDao().skipWhile { it.isEmpty()},
                Observable.fromArray(getPlacesFromApi(sharedPref.getUserId()!!))
            ).run {
                subscribeOn(Schedulers.io())
                observeOn(AndroidSchedulers.mainThread())
                subscribe()
            }
        )
    }

    private fun getPlacesFromDao() =
        placeDAO.getAllPlaces().run {
            subscribeOn(Schedulers.io())
            observeOn(AndroidSchedulers.mainThread())
            doOnNext {
                places.value = it
            }
        }


    fun getPlacesFromApi(userId: String) {
        loadSubscriptions.add(
            apiService.getPlaces(userId)
                .onErrorResumeNext(Observable.empty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {isLoading.value = true }
                .doOnTerminate {isLoading.value = false}
                .map { it ->
                    it.map {
                        PlaceDTO(
                            it.key,
                            it.value.placeName,
                            it.value.placeCoordinates
                        ) }.sortedBy { it.placeName }
                }
                .doOnNext {
                    places.value = it
                    placeDAO.insertAll(it)
                }
                .subscribe()
        )
    }

    fun deletePlace(userId: String, placeId: String) {
        deleteSubscriptions.add(
            apiService.deletePlace(userId, placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {isLoading.value = true }
                .doOnTerminate {isLoading.value = false}
                .subscribe(
                    {},
                    {errorLiveEvent.value = it}
                )
        )
    }

    override fun onCleared() {
        loadSubscriptions.dispose()
        deleteSubscriptions.dispose()
        super.onCleared()
    }
}