package com.mapinspector.di

import android.app.Application
import com.mapinspector.di.network.ViewModelModule
import com.mapinspector.di.network.NetworkModule

class App: Application() {

    companion object{
        lateinit var appComponent: AppComponent
    }


    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = with(DaggerAppComponent.builder()) {
            networkModule(NetworkModule)
            viewModelModule(ViewModelModule)
        }.build()
    }
}