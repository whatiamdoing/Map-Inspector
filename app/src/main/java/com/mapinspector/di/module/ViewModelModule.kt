package com.mapinspector.di.module

import com.mapinspector.viewmodel.BottomDialogViewModel
import com.mapinspector.viewmodel.MapListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ViewModelModule {

    @Provides
    @Singleton
    fun provideBottomDialogViewModel(): BottomDialogViewModel {
        return BottomDialogViewModel()
    }

    @Provides
    @Singleton
    fun provideMapListViewModel(): MapListViewModel {
        return MapListViewModel()
    }
}