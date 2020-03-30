package com.mapinspector.di

import android.app.Application
import com.mapinspector.di.module.*

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
            roomModule(RoomModule)
        }.build()
    }
}