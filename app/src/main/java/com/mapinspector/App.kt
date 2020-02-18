package com.mapinspector

import android.app.Application
import com.mapinspector.di.AppComponent
import com.mapinspector.di.DaggerAppComponent
import com.mapinspector.di.network.NetworkModule

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initializeDagger()
    }

    private fun initializeDagger() {
        appComponent = with(DaggerAppComponent.builder()) {
            networkModule(NetworkModule)
        }.build()
    }
}