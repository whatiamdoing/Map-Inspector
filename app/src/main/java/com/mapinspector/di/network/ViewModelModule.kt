package com.mapinspector.di.network

import com.mapinspector.ui.map.viewmodels.BottomDialogViewModel
import com.mapinspector.ui.map.viewmodels.MapViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ViewModelModule {

    @Provides
    @Singleton
    fun provideMapViewModel(): MapViewModel {
        return MapViewModel()
    }

    @Provides
    @Singleton
    fun provideBottomDialogViewModel(): BottomDialogViewModel {
        return BottomDialogViewModel()
    }
}