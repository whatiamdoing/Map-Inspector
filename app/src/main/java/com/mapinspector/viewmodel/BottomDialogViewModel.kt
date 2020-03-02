package com.mapinspector.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapinspector.di.App
import com.mapinspector.di.network.ApiService
import com.mapinspector.model.Coordinates
import com.mapinspector.model.Place
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BottomDialogViewModel: ViewModel() {

    @Inject
    lateinit var apiService: ApiService

    init {
        App.appComponent.inject(this)
    }
    private val subscriptions = CompositeDisposable()
    val errorLiveData = MutableLiveData<Throwable>()
    val isLoading:  MutableLiveData<Boolean> = MutableLiveData()

    fun createPlace(id: String, placeName: String, placeCoordinates: Coordinates, placeId: String){
        subscriptions.add(
            apiService.createPlace(id, placeId, Place(placeName, placeCoordinates))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {isLoading.value = true}
                .doOnTerminate {isLoading.value = false}
                .subscribe({},
                    {errorLiveData.value = it})
        )
    }
}