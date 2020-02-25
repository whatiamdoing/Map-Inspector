package com.mapinspector.di

import android.app.Application
import com.mapinspector.di.module.ContextModule
import com.mapinspector.di.module.ViewModelModule
import com.mapinspector.di.module.NetworkModule
import com.mapinspector.di.module.SharedPreferencesModule

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
            sharedPreferencesModule(SharedPreferencesModule)
            contextModule(ContextModule(applicationContext))
        }.build()
    }
}