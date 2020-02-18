package com.mapinspector.base

import androidx.lifecycle.ViewModel
import com.mapinspector.di.AppComponent
import com.mapinspector.di.DaggerAppComponent
import com.mapinspector.di.network.NetworkModule

class BaseViewModel: ViewModel() {
    private val injector: AppComponent = DaggerAppComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

}