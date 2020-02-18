package com.mapinspector.di

import com.mapinspector.ui.map.MapViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ViewModelModule {

    @Provides
    @Singleton
    fun provideBaseViewModel(): MapViewModel {
        return MapViewModel()
    }
}