package com.mapinspector.di.module
import android.content.Context
import com.mapinspector.utils.Constants.SharedPref.PREF_KEY_USER
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
object SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) =
        context.getSharedPreferences(PREF_KEY_USER, Context.MODE_PRIVATE)!!
}