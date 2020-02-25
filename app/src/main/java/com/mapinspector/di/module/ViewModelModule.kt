package com.mapinspector.di.module

import com.mapinspector.viewmodels.BottomDialogViewModel
import com.mapinspector.viewmodels.MapViewModel
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