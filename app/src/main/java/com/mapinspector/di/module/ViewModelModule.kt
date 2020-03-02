package com.mapinspector.di.module

import com.mapinspector.viewmodel.BottomDialogViewModel
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
}