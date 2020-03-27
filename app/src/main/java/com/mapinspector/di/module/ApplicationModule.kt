package com.mapinspector.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var application: Application) {

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return application
    }

}