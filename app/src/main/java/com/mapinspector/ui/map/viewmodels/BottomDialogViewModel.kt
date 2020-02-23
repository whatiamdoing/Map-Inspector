package com.mapinspector.ui.map.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapinspector.di.App
import com.mapinspector.di.network.ApiService
import com.mapinspector.entity.YourPlace
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
    val errorLiveData = MutableLiveData<String>()

    fun createPlace(id: String, placeName: String, placeCoordinates: String, placeNumber: Int){
        subscriptions.add(
            apiService.createPlace(id, placeNumber, YourPlace(placeName, placeCoordinates))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {}
                .doOnTerminate {}
                .subscribe(
                    { },
                    { } ))
    }
}