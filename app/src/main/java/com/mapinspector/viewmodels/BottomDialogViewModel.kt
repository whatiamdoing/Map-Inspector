package com.mapinspector.viewmodels

import androidx.lifecycle.ViewModel
import com.mapinspector.di.App
import com.mapinspector.di.network.ApiService
import com.mapinspector.entity.Coordinates
import com.mapinspector.entity.Place
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

    fun createPlace(id: String, placeName: String, placeCoordinates: Coordinates, placeId: String){
        subscriptions.add(
            apiService.createPlace(id, placeId, Place(placeName, placeCoordinates))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({},{})
        )
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }
}